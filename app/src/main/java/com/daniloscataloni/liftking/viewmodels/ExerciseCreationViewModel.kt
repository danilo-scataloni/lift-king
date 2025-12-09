package com.daniloscataloni.liftking.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniloscataloni.liftking.data.entities.ExerciseEntity
import com.daniloscataloni.liftking.entities.Exercise
import com.daniloscataloni.liftking.entities.MuscleGroup
import com.daniloscataloni.liftking.repositories.ExerciseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExerciseCreationViewModel(
    val repository: ExerciseRepository
) : ViewModel() {

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
        if (uiState.value.exerciseName.isNullOrBlank()) {
            return
        }
        viewModelScope.launch {
            repository.insertExercise(uiState.value.toExerciseEntity())
        }

    }

}

data class ExerciseCreationState(
    val primaryMuscleGroup: MuscleGroup? = null,
    val secondaryMuscleGroup: MuscleGroup? = null,
    val exerciseName: String? = null
){
    fun toExerciseEntity(): ExerciseEntity {
        return ExerciseEntity(
            description = exerciseName ?: "",
            primaryMuscleGroup = primaryMuscleGroup
                ?: throw IllegalStateException("Primary muscle group must be selected"),
            secondaryMuscleGroups = secondaryMuscleGroup
        )
    }
}