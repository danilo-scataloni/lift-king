package com.daniloscataloni.liftking.viewmodels

import androidx.lifecycle.ViewModel
import com.daniloscataloni.liftking.entities.MuscleGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExerciseCreationViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(ExerciseCreationState())
    val uiState: StateFlow<ExerciseCreationState> = _uiState

    fun onPrimaryMuscleSelected(muscleGroup: MuscleGroup) {
        _uiState.value = _uiState.value.copy(primaryMuscleGroup = muscleGroup)
    }

    fun onSecondaryMuscleSelected(muscleGroup: MuscleGroup) {
        _uiState.value = _uiState.value.copy(secondaryMuscleGroup = muscleGroup)
    }

    fun onExerciseNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(exerciseName = newName)
    }

    fun onSaveExercise() {
        // Logic to save the exercise can be implemented here
    }

}

data class ExerciseCreationState(
    val primaryMuscleGroup: MuscleGroup? = null,
    val secondaryMuscleGroup: MuscleGroup? = null,
    val exerciseName: String? = null
)