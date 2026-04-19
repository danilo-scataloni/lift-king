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
import kotlinx.coroutines.flow.MutableStateFlow
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
    private lateinit var exercisesFlow: MutableStateFlow<List<Exercise>>

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = mockk()
        exercisesFlow = MutableStateFlow(emptyList())
        every { repository.getAllExercises() } returns exercisesFlow
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

    @Test
    fun `muscle filter includes exercises where muscle is primary or secondary`() = runTest {
        exercisesFlow.value = listOf(
            exercise(
                id = 1,
                description = "Remada",
                primary = MuscleGroup.BACK,
                secondary = MuscleGroup.BICEPS
            ),
            exercise(
                id = 2,
                description = "Rosca Alternada",
                primary = MuscleGroup.BICEPS
            ),
            exercise(
                id = 3,
                description = "Supino",
                primary = MuscleGroup.CHEST,
                secondary = MuscleGroup.TRICEPS
            )
        )
        advanceUntilIdle()

        viewModel.onMuscleFilterSelected(MuscleGroup.BICEPS)

        assertEquals(MuscleGroup.BICEPS, viewModel.uiState.value.selectedMuscleFilter)
        assertEquals(
            listOf("Remada", "Rosca Alternada"),
            viewModel.uiState.value.exercises.map { it.description }
        )
        assertEquals(3, viewModel.uiState.value.allExercisesCount)
    }

    @Test
    fun `clear muscle filter restores sorted full exercise list`() = runTest {
        exercisesFlow.value = listOf(
            exercise(id = 1, description = "Supino", primary = MuscleGroup.CHEST),
            exercise(id = 2, description = "Agachamento", primary = MuscleGroup.QUADS),
            exercise(id = 3, description = "Remada", primary = MuscleGroup.BACK)
        )
        advanceUntilIdle()

        viewModel.onMuscleFilterSelected(MuscleGroup.CHEST)
        viewModel.onMuscleFilterSelected(null)

        assertEquals(null, viewModel.uiState.value.selectedMuscleFilter)
        assertEquals(
            listOf("Agachamento", "Remada", "Supino"),
            viewModel.uiState.value.exercises.map { it.description }
        )
    }

    private fun exercise(
        id: Int,
        description: String,
        primary: MuscleGroup,
        secondary: MuscleGroup? = null,
        weightUnit: WeightUnit = WeightUnit.KG
    ) = Exercise(
        id = id,
        description = description,
        primaryMuscleGroup = primary,
        secondaryMuscleGroups = secondary,
        weightUnit = weightUnit
    )
}
