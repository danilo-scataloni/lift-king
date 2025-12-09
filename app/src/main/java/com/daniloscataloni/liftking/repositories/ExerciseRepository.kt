package com.daniloscataloni.liftking.repositories

import com.daniloscataloni.liftking.data.daos.ExerciseDao
import com.daniloscataloni.liftking.data.entities.ExerciseEntity

class ExerciseRepository(
    val exerciseDao: ExerciseDao
) {

    suspend fun insertExercise(exercise: ExerciseEntity){
        exerciseDao.insertExercise(exercise)
    }
}