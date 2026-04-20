package com.daniloscataloni.liftking.ui.viewmodels

import com.daniloscataloni.liftking.data.repositories.IExerciseRepository
import com.daniloscataloni.liftking.data.repositories.ITrainingRepository
import com.daniloscataloni.liftking.data.repositories.IWorkoutRepository
import com.daniloscataloni.liftking.domain.models.RestTimer
import com.daniloscataloni.liftking.domain.models.TrainingSession
import com.daniloscataloni.liftking.domain.models.Workout
import com.daniloscataloni.liftking.resttimer.AppVisibilityTracker
import com.daniloscataloni.liftking.resttimer.IRestTimerManager
import com.daniloscataloni.liftking.resttimer.RestTimerScheduleMode
import com.daniloscataloni.liftking.resttimer.RestTimerScheduleResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrainingViewModelRestTimerTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var workoutRepository: IWorkoutRepository
    private lateinit var trainingRepository: ITrainingRepository
    private lateinit var exerciseRepository: IExerciseRepository
    private lateinit var restTimerManager: IRestTimerManager
    private lateinit var appVisibilityTracker: AppVisibilityTracker
    private lateinit var viewModel: TrainingViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)

        workoutRepository = mockk()
        trainingRepository = mockk()
        exerciseRepository = mockk()
        restTimerManager = mockk(relaxed = true)
        appVisibilityTracker = mockk(relaxed = true)

        coEvery { workoutRepository.getWorkoutById(1L) } returns Workout(
            id = 1L,
            periodizationId = 1L,
            name = "Treino A"
        )
        every { workoutRepository.getExercisesForWorkout(1L) } returns MutableStateFlow(emptyList())
        every { exerciseRepository.getAllExercises() } returns MutableStateFlow(emptyList())
        coEvery { trainingRepository.getInProgressSession(1L) } returns TrainingSession(
            id = 99L,
            workoutId = 1L
        )
        coEvery { trainingRepository.getExerciseLogs(99L) } returns emptyList()
        every { restTimerManager.getActiveTimer() } returns null
        every { restTimerManager.getLastUsedDurationSeconds() } returns 90
        every { appVisibilityTracker.isAppInForeground } returns true

        viewModel = TrainingViewModel(
            workoutRepository = workoutRepository,
            trainingRepository = trainingRepository,
            exerciseRepository = exerciseRepository,
            restTimerManager = restTimerManager,
            appVisibilityTracker = appVisibilityTracker
        )
    }

    @AfterEach
    fun tearDown() {
        viewModel.cancelRestTimer()
        Dispatchers.resetMain()
    }

    @Test
    fun `startRestTimer schedules timer and exposes active state`() = runTest {
        every { restTimerManager.scheduleRestTimer(any()) } answers {
            RestTimerScheduleResult(
                timer = firstArg(),
                scheduleMode = RestTimerScheduleMode.EXACT
            )
        }

        viewModel.loadWorkout(1L)
        advanceUntilIdle()

        viewModel.startRestTimer(
            exerciseId = 7,
            exerciseName = "Supino",
            durationSeconds = 90
        )

        val activeRestTimer = viewModel.uiState.value.activeRestTimer
        assertNotNull(activeRestTimer)
        assertEquals("Supino", activeRestTimer?.exerciseName)
        assertEquals(90, viewModel.uiState.value.lastRestDurationSeconds)
        assertFalse(viewModel.uiState.value.showExactAlarmPermissionPrompt)

        coVerify(exactly = 0) { trainingRepository.startSession(any()) }
        io.mockk.verify {
            restTimerManager.scheduleRestTimer(
                withArg<RestTimer> { timer ->
                    assertEquals(1L, timer.workoutId)
                    assertEquals("Treino A", timer.workoutName)
                    assertEquals(7, timer.exerciseId)
                    assertEquals(90, timer.durationSeconds)
                }
            )
        }

        viewModel.cancelRestTimer()
    }

    @Test
    fun `startRestTimer shows exact alarm prompt when manager falls back to inexact`() = runTest {
        every { restTimerManager.scheduleRestTimer(any()) } answers {
            RestTimerScheduleResult(
                timer = firstArg(),
                scheduleMode = RestTimerScheduleMode.INEXACT
            )
        }

        viewModel.loadWorkout(1L)
        advanceUntilIdle()

        viewModel.startRestTimer(
            exerciseId = 3,
            exerciseName = "Agachamento",
            durationSeconds = 120
        )

        assertTrue(viewModel.uiState.value.showExactAlarmPermissionPrompt)

        viewModel.cancelRestTimer()
    }

    @Test
    fun `cancelRestTimer clears active state and cancels manager timer`() = runTest {
        every { restTimerManager.scheduleRestTimer(any()) } answers {
            RestTimerScheduleResult(
                timer = firstArg(),
                scheduleMode = RestTimerScheduleMode.EXACT
            )
        }

        viewModel.loadWorkout(1L)
        advanceUntilIdle()

        viewModel.startRestTimer(
            exerciseId = 5,
            exerciseName = "Remada",
            durationSeconds = 60
        )
        viewModel.cancelRestTimer()

        assertNull(viewModel.uiState.value.activeRestTimer)
        assertFalse(viewModel.uiState.value.showExactAlarmPermissionPrompt)
        io.mockk.verify { restTimerManager.cancelActiveTimer() }
    }

    @Test
    fun `onExactAlarmSettingsResult reschedules active timer and hides prompt`() = runTest {
        every { restTimerManager.scheduleRestTimer(any()) } answers {
            RestTimerScheduleResult(
                timer = firstArg(),
                scheduleMode = RestTimerScheduleMode.INEXACT
            )
        }
        every { restTimerManager.rescheduleActiveTimer() } returns RestTimerScheduleResult(
            timer = RestTimer(
                workoutId = 1L,
                exerciseId = 5,
                exerciseName = "Remada",
                workoutName = "Treino A",
                durationSeconds = 60,
                startAtEpochMillis = System.currentTimeMillis(),
                endAtEpochMillis = System.currentTimeMillis() + 60_000L
            ),
            scheduleMode = RestTimerScheduleMode.EXACT
        )

        viewModel.loadWorkout(1L)
        advanceUntilIdle()
        viewModel.startRestTimer(
            exerciseId = 5,
            exerciseName = "Remada",
            durationSeconds = 60
        )

        assertTrue(viewModel.uiState.value.showExactAlarmPermissionPrompt)

        viewModel.onExactAlarmSettingsResult()

        assertFalse(viewModel.uiState.value.showExactAlarmPermissionPrompt)
        assertEquals("Remada", viewModel.uiState.value.activeRestTimer?.exerciseName)
        io.mockk.verify { restTimerManager.rescheduleActiveTimer() }
    }

    @Test
    fun `completeSession cancels active rest timer before finishing workout`() = runTest {
        every { restTimerManager.scheduleRestTimer(any()) } answers {
            RestTimerScheduleResult(
                timer = firstArg(),
                scheduleMode = RestTimerScheduleMode.EXACT
            )
        }
        coEvery { trainingRepository.completeSession(99L) } returns Unit

        viewModel.loadWorkout(1L)
        advanceUntilIdle()
        viewModel.startRestTimer(
            exerciseId = 11,
            exerciseName = "Puxada",
            durationSeconds = 75
        )

        viewModel.completeSession()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.activeRestTimer)
        assertNull(viewModel.uiState.value.sessionId)
        io.mockk.verify { restTimerManager.cancelActiveTimer() }
        coVerify { trainingRepository.completeSession(99L) }
    }
}
