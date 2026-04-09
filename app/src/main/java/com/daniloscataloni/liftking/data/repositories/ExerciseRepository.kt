package com.daniloscataloni.liftking.data.repositories

import com.daniloscataloni.liftking.data.daos.ExerciseDao
import com.daniloscataloni.liftking.data.mappers.toDomain
import com.daniloscataloni.liftking.data.mappers.toEntity
import com.daniloscataloni.liftking.domain.models.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface IExerciseRepository {
    suspend fun insertExercise(exercise: Exercise): Long
    fun getAllExercises(): Flow<List<Exercise>>
    suspend fun updateExercise(exercise: Exercise)
    suspend fun deleteExercise(exercise: Exercise)
}


class ExerciseRepository(
    val exerciseDao: ExerciseDao
) : IExerciseRepository {


    override suspend fun insertExercise(exercise: Exercise): Long {
        return exerciseDao.insertExercise(exercise.toEntity())
    }

    override fun getAllExercises(): Flow<List<Exercise>> {
        return exerciseDao.getAllExercises()
            .map { exerciseEntities ->
                exerciseEntities.map { it.toDomain() }
            }
    }

    override suspend fun updateExercise(exercise: Exercise) {
        exerciseDao.updateExercise(exercise.toEntity())
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        exerciseDao.deleteExercise(exercise.toEntity())
    }
}
