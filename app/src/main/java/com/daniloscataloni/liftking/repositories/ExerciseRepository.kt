package com.daniloscataloni.liftking.repositories

import com.daniloscataloni.liftking.data.daos.ExerciseDao
import com.daniloscataloni.liftking.data.mappers.toDomain
import com.daniloscataloni.liftking.data.mappers.toEntity
import com.daniloscataloni.liftking.entities.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface IExerciseRepository {
    suspend fun insertExercise(exercise: Exercise): Long
    fun getAllExercises(): Flow<List<Exercise>>
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
}
