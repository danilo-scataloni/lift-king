package com.daniloscataloni.liftking.viewmodels

import androidx.lifecycle.ViewModel
import com.daniloscataloni.liftking.entities.Exercise
import com.daniloscataloni.liftking.repositories.IExerciseRepository
import kotlinx.coroutines.flow.Flow

class ExerciseListViewModel(
    private val repository: IExerciseRepository
): ViewModel() {

    fun getAllExercises(): Flow<List<Exercise>> {
        return repository.getAllExercises()
    }
}