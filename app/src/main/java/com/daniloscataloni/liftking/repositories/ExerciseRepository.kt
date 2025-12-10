package com.daniloscataloni.liftking.repositories

import com.daniloscataloni.liftking.data.daos.ExerciseDao
import com.daniloscataloni.liftking.data.entities.ExerciseEntity
import com.daniloscataloni.liftking.entities.Exercise

interface IExerciseRepository {
    suspend fun insertExercise(exercise: Exercise)
}


class ExerciseRepository(
    val exerciseDao: ExerciseDao
) : IExerciseRepository {
    override suspend fun insertExercise(exercise: Exercise){
        exerciseDao.insertExercise(exercise.toExerciseEntity())
    }
}

private fun Exercise.toExerciseEntity(): ExerciseEntity {
    return ExerciseEntity(
        id = this.id,
        description = this.description,
        primaryMuscleGroup = this.primaryMuscleGroup,
        secondaryMuscleGroups = this.secondaryMuscleGroups
    )
}

private fun ExerciseEntity.toExercise(): Exercise {
    return Exercise(
        id = this.id,
        description = this.description,
        primaryMuscleGroup = this.primaryMuscleGroup,
        secondaryMuscleGroups = this.secondaryMuscleGroups
    )
}