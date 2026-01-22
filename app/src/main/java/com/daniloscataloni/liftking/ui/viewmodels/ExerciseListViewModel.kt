package com.daniloscataloni.liftking.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.daniloscataloni.liftking.domain.models.Exercise
import com.daniloscataloni.liftking.data.repositories.IExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExerciseListViewModel(
    private val repository: IExerciseRepository
): ViewModel() {

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    fun getAllExercises(): Flow<List<Exercise>> {
        return repository.getAllExercises()
    }

    fun onShowDialog(){
        _showDialog.value = true
    }

    fun onDismissDialog(){
        _showDialog.value = false
    }

}