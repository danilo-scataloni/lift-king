package com.daniloscataloni.liftking.ui.viewmodels

import com.daniloscataloni.liftking.data.repositories.IExerciseRepository
import com.daniloscataloni.liftking.domain.models.Exercise
import com.daniloscataloni.liftking.domain.models.MuscleGroup
import com.daniloscataloni.liftking.domain.models.WeightUnit
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExercisesViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: IExerciseRepository
    private lateinit var viewModel: ExercisesViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = mockk()
        every { repository.getAllExercises() } returns flowOf(emptyList())
        viewModel = ExercisesViewModel(repository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `showCreateDialog resets form to defaults`() {
        viewModel.onNewExerciseNameChange("Agachamento")
        viewModel.onNewExercisePrimaryMuscleChange(MuscleGroup.QUADS)
        viewModel.onNewExerciseSecondaryMuscleChange(MuscleGroup.HAMSTRINGS)
        viewModel.onNewExerciseWeightUnitChange(WeightUnit.LIBRAS)

        viewModel.setCreateDialogVisible(true)

        val uiState = viewModel.uiState.value

        assertTrue(uiState.showCreateDialog)
        assertEquals("", uiState.newExerciseName)
        assertEquals(MuscleGroup.CHEST, uiState.newExercisePrimaryMuscle)
        assertEquals(null, uiState.newExerciseSecondaryMuscle)
        assertEquals(WeightUnit.KG, uiState.newExerciseWeightUnit)
    }

    @Test
    fun `createExercise persists new exercise and closes dialog`() = runTest {
        coEvery { repository.insertExercise(any()) } returns 1L

        viewModel.setCreateDialogVisible(true)
        viewModel.onNewExerciseNameChange("Remada")
        viewModel.onNewExercisePrimaryMuscleChange(MuscleGroup.BACK)
        viewModel.onNewExerciseSecondaryMuscleChange(MuscleGroup.BICEPS)
        viewModel.onNewExerciseWeightUnitChange(WeightUnit.TIJOLINHOS)

        viewModel.createExercise()
        advanceUntilIdle()

        coVerify {
            repository.insertExercise(
                Exercise(
                    id = 0,
                    description = "Remada",
                    primaryMuscleGroup = MuscleGroup.BACK,
                    secondaryMuscleGroups = MuscleGroup.BICEPS,
                    weightUnit = WeightUnit.TIJOLINHOS
                )
            )
        }
        assertFalse(viewModel.uiState.value.showCreateDialog)
    }

    @Test
    fun `deleteExercise exposes error when repository throws`() = runTest {
        val exercise = Exercise(
            id = 1,
            description = "Supino",
            primaryMuscleGroup = MuscleGroup.CHEST,
            secondaryMuscleGroups = null,
            weightUnit = WeightUnit.KG
        )
        coEvery { repository.deleteExercise(exercise) } throws IllegalStateException("in use")

        viewModel.deleteExercise(exercise)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.deleteExerciseError)
    }
}
