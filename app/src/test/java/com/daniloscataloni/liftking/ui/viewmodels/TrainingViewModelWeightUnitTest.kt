package com.daniloscataloni.liftking.ui.viewmodels

import com.daniloscataloni.liftking.domain.models.Exercise
import com.daniloscataloni.liftking.domain.models.MuscleGroup
import com.daniloscataloni.liftking.domain.models.SetLog
import com.daniloscataloni.liftking.domain.models.WeightUnit
import com.daniloscataloni.liftking.domain.models.WorkoutExercise
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TrainingViewModelWeightUnitTest {

    private lateinit var viewModel: TrainingViewModel

    @BeforeEach
    fun setup() {
        viewModel = TrainingViewModel(
            workoutRepository = mockk(relaxed = true),
            trainingRepository = mockk(relaxed = true),
            exerciseRepository = mockk(relaxed = true)
        )
    }

    @Test
    fun `default newExerciseWeightUnit is KG`() {
        assertEquals(WeightUnit.KG, viewModel.uiState.value.newExerciseWeightUnit)
    }

    @Test
    fun `onNewExerciseWeightUnitChange updates state`() {
        viewModel.onNewExerciseWeightUnitChange(WeightUnit.TIJOLINHOS)
        assertEquals(WeightUnit.TIJOLINHOS, viewModel.uiState.value.newExerciseWeightUnit)
    }

    @Test
    fun `showCreateExerciseDialog resets weightUnit to KG`() {
        viewModel.onNewExerciseWeightUnitChange(WeightUnit.LIBRAS)
        viewModel.showCreateExerciseDialog()
        assertEquals(WeightUnit.KG, viewModel.uiState.value.newExerciseWeightUnit)
    }

    @Test
    fun `startEditingSet stores weightUnit of owning exercise`() {
        val exercise = Exercise(
            id = 1,
            description = "Supino",
            primaryMuscleGroup = MuscleGroup.CHEST,
            secondaryMuscleGroups = null,
            weightUnit = WeightUnit.TIJOLINHOS
        )
        val set = SetLog(id = 10L, exerciseLogId = 1L, setNumber = 1, weight = 5f, reps = 10)
        val exerciseWithSets = ExerciseWithSets(
            workoutExercise = WorkoutExercise(
                id = 1L,
                workoutId = 1L,
                exerciseId = 1,
                order = 0,
                targetSets = null
            ),
            exercise = exercise,
            lastSets = emptyList(),
            currentSets = listOf(set)
        )
        viewModel.seedExercisesForTest(listOf(exerciseWithSets))

        viewModel.startEditingSet(set)

        assertEquals(WeightUnit.TIJOLINHOS, viewModel.uiState.value.editingSetWeightUnit)
    }

    @Test
    fun `startEditingSet falls back to KG when exercise not found`() {
        val set = SetLog(id = 99L, exerciseLogId = 99L, setNumber = 1, weight = 10f, reps = 5)

        viewModel.startEditingSet(set)

        assertEquals(WeightUnit.KG, viewModel.uiState.value.editingSetWeightUnit)
    }

    @Test
    fun `cancelEditingSet resets editingSetWeightUnit to KG`() {
        val exercise = Exercise(
            id = 1,
            description = "Test",
            primaryMuscleGroup = MuscleGroup.CHEST,
            secondaryMuscleGroups = null,
            weightUnit = WeightUnit.TIJOLINHOS
        )
        val set = SetLog(id = 5L, exerciseLogId = 1L, setNumber = 1, weight = 10f, reps = 8)
        viewModel.seedExercisesForTest(listOf(
            ExerciseWithSets(
                workoutExercise = WorkoutExercise(id = 1L, workoutId = 1L, exerciseId = 1, order = 0, targetSets = null),
                exercise = exercise,
                lastSets = emptyList(),
                currentSets = listOf(set)
            )
        ))
        viewModel.startEditingSet(set)
        assertEquals(WeightUnit.TIJOLINHOS, viewModel.uiState.value.editingSetWeightUnit)

        viewModel.cancelEditingSet()

        assertEquals(WeightUnit.KG, viewModel.uiState.value.editingSetWeightUnit)
    }
}
