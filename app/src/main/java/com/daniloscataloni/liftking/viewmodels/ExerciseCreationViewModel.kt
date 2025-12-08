package com.daniloscataloni.liftking.viewmodels

import androidx.lifecycle.ViewModel
import com.daniloscataloni.liftking.entities.MuscleGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExerciseCreationViewModel() : ViewModel() {

    private val _primaryMuscleGroup = MutableStateFlow<MuscleGroup?>(null)
    val primaryMuscleGroup: StateFlow<MuscleGroup?> = _primaryMuscleGroup

    private val _secondaryMuscleGroup = MutableStateFlow<MuscleGroup?>(null)
    val secondaryMuscleGroup: StateFlow<MuscleGroup?> = _secondaryMuscleGroup

    private val _exerciseName = MutableStateFlow("")
    val exerciseName: StateFlow<String> = _exerciseName

    fun onMuscleGroup1Selected(muscleGroup: MuscleGroup) {
        _primaryMuscleGroup.value = muscleGroup
    }

    fun onMuscleGroup2Selected(muscleGroup: MuscleGroup) {
        _secondaryMuscleGroup.value = muscleGroup
    }

    fun onExerciseNameChange(newName: String) {
        _exerciseName.value = newName
    }

    fun onSaveExercise() {
        // Logic to save the exercise can be implemented here
    }

}