package com.daniloscataloni.liftking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniloscataloni.liftking.entities.Exercise
import com.daniloscataloni.liftking.entities.MuscleGroup
import com.daniloscataloni.liftking.repositories.IExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExerciseCreationViewModel(
    val repository: IExerciseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExerciseCreationState())
    val uiState: StateFlow<ExerciseCreationState> = _uiState

    fun onPrimaryMuscleSelected(muscleGroup: MuscleGroup) {
        if (muscleGroup == _uiState.value.exercise.secondaryMuscleGroups) {
            // Prevent selecting the same muscle group for both primary and secondary
            return
        }
        _uiState.value = _uiState.value.copy(
            exercise = _uiState.value.exercise.copy(
                primaryMuscleGroup = muscleGroup
            )
        )
    }

    fun onSecondaryMuscleSelected(muscleGroup: MuscleGroup) {

        if (muscleGroup == _uiState.value.exercise.secondaryMuscleGroups) {
            _uiState.value = _uiState.value.copy(
                exercise = _uiState.value.exercise.copy(
                    secondaryMuscleGroups = null
                )
            )
            return
        }
        if (muscleGroup == _uiState.value.exercise.primaryMuscleGroup) {
            return
        }

        _uiState.value = _uiState.value.copy(
            exercise = _uiState.value.exercise.copy(
                secondaryMuscleGroups = muscleGroup
            )
        )
    }

    fun onExerciseNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(
            exercise = _uiState.value.exercise.copy(
                description = newName
            )
        )
    }

    fun onSaveExercise() {
        val currentExercise = uiState.value.exercise
        if (currentExercise.description.isBlank()) {
            return
        }

        viewModelScope.launch {
            repository.insertExercise(currentExercise)
        }

        _uiState.value = ExerciseCreationState()
    }

}

data class ExerciseCreationState(
    val exercise: Exercise = Exercise(
        id = 0,
        description = "",
        primaryMuscleGroup = MuscleGroup.CHEST,
        secondaryMuscleGroups = null
    )
)