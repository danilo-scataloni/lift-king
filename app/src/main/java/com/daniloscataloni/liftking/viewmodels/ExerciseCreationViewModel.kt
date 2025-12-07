package com.daniloscataloni.liftking.viewmodels

import androidx.lifecycle.ViewModel
import com.daniloscataloni.liftking.entities.MuscleGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ExerciseCreationViewModel() : ViewModel() {

    private val _selectedMuscleGroup = MutableStateFlow<MuscleGroup?>(null)
    val selectedMuscleGroup: StateFlow<MuscleGroup?> = _selectedMuscleGroup

    fun onMuscleGroupClick(muscleGroup: MuscleGroup) {
        val actualMuscleGroup = _selectedMuscleGroup.value
        if (muscleGroup == actualMuscleGroup) {
            _selectedMuscleGroup.value = null
        } else {
            _selectedMuscleGroup.value = muscleGroup
        }
    }

}