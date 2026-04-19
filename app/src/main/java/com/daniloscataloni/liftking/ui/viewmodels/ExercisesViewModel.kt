package com.daniloscataloni.liftking.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniloscataloni.liftking.data.repositories.IExerciseRepository
import com.daniloscataloni.liftking.domain.models.Exercise
import com.daniloscataloni.liftking.domain.models.MuscleGroup
import com.daniloscataloni.liftking.domain.models.WeightUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExercisesUiState(
    val exercises: List<Exercise> = emptyList(),
    val showCreateDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val newExerciseName: String = "",
    val newExercisePrimaryMuscle: MuscleGroup = MuscleGroup.CHEST,
    val newExerciseSecondaryMuscle: MuscleGroup? = null,
    val newExerciseWeightUnit: WeightUnit = WeightUnit.KG,
    val editExerciseName: String = "",
    val editExercisePrimaryMuscle: MuscleGroup = MuscleGroup.CHEST,
    val editExerciseSecondaryMuscle: MuscleGroup? = null,
    val editExerciseWeightUnit: WeightUnit = WeightUnit.KG,
    val deleteExerciseError: Boolean = false
)

class ExercisesViewModel(
    private val repository: IExerciseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExercisesUiState())
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    private var exerciseToEdit: Exercise? = null

    init {
        repository.getAllExercises()
            .map { exercises -> exercises.sortedBy { it.description.lowercase() } }
            .onEach { exercises ->
                _uiState.update { currentState ->
                    currentState.copy(exercises = exercises)
                }
            }
            .launchIn(viewModelScope)
    }

    fun setCreateDialogVisible(isVisible: Boolean) {
        _uiState.update { currentState ->
            if (isVisible) {
                currentState.copy(
                    showCreateDialog = true,
                    newExerciseName = "",
                    newExercisePrimaryMuscle = MuscleGroup.CHEST,
                    newExerciseSecondaryMuscle = null,
                    newExerciseWeightUnit = WeightUnit.KG
                )
            } else {
                currentState.copy(showCreateDialog = false)
            }
        }
    }

    fun onNewExerciseNameChange(name: String) {
        _uiState.update { currentState -> currentState.copy(newExerciseName = name) }
    }

    fun onNewExercisePrimaryMuscleChange(muscle: MuscleGroup) {
        if (muscle == _uiState.value.newExerciseSecondaryMuscle) return
        _uiState.update { currentState -> currentState.copy(newExercisePrimaryMuscle = muscle) }
    }

    fun onNewExerciseSecondaryMuscleChange(muscle: MuscleGroup?) {
        if (muscle == _uiState.value.newExercisePrimaryMuscle) return
        _uiState.update { currentState -> currentState.copy(newExerciseSecondaryMuscle = muscle) }
    }

    fun onNewExerciseWeightUnitChange(unit: WeightUnit) {
        _uiState.update { currentState -> currentState.copy(newExerciseWeightUnit = unit) }
    }

    fun createExercise() {
        val state = _uiState.value
        val name = state.newExerciseName.trim()
        if (name.isBlank()) return

        viewModelScope.launch {
            repository.insertExercise(
                Exercise(
                    id = 0,
                    description = name,
                    primaryMuscleGroup = state.newExercisePrimaryMuscle,
                    secondaryMuscleGroups = state.newExerciseSecondaryMuscle,
                    weightUnit = state.newExerciseWeightUnit
                )
            )
            setCreateDialogVisible(false)
        }
    }

    fun setExerciseForEditing(exercise: Exercise?) {
        exerciseToEdit = exercise
        _uiState.update { currentState ->
            if (exercise != null) {
                currentState.copy(
                    showEditDialog = true,
                    editExerciseName = exercise.description,
                    editExercisePrimaryMuscle = exercise.primaryMuscleGroup,
                    editExerciseSecondaryMuscle = exercise.secondaryMuscleGroups,
                    editExerciseWeightUnit = exercise.weightUnit
                )
            } else {
                currentState.copy(showEditDialog = false)
            }
        }
    }

    fun onEditExerciseNameChange(name: String) {
        _uiState.update { currentState -> currentState.copy(editExerciseName = name) }
    }

    fun onEditExercisePrimaryMuscleChange(muscle: MuscleGroup) {
        if (muscle == _uiState.value.editExerciseSecondaryMuscle) return
        _uiState.update { currentState -> currentState.copy(editExercisePrimaryMuscle = muscle) }
    }

    fun onEditExerciseSecondaryMuscleChange(muscle: MuscleGroup?) {
        if (muscle == _uiState.value.editExercisePrimaryMuscle) return
        _uiState.update { currentState -> currentState.copy(editExerciseSecondaryMuscle = muscle) }
    }

    fun onEditExerciseWeightUnitChange(unit: WeightUnit) {
        _uiState.update { currentState -> currentState.copy(editExerciseWeightUnit = unit) }
    }

    fun confirmEditExercise() {
        val exercise = exerciseToEdit ?: return
        val state = _uiState.value
        val name = state.editExerciseName.trim()
        if (name.isBlank()) return

        viewModelScope.launch {
            repository.updateExercise(
                exercise.copy(
                    description = name,
                    primaryMuscleGroup = state.editExercisePrimaryMuscle,
                    secondaryMuscleGroups = state.editExerciseSecondaryMuscle,
                    weightUnit = state.editExerciseWeightUnit
                )
            )
            setExerciseForEditing(null)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            try {
                repository.deleteExercise(exercise)
                _uiState.update { currentState -> currentState.copy(deleteExerciseError = false) }
            } catch (_: Exception) {
                _uiState.update { currentState -> currentState.copy(deleteExerciseError = true) }
            }
        }
    }

    fun clearDeleteExerciseError() {
        _uiState.update { currentState -> currentState.copy(deleteExerciseError = false) }
    }
}
