package com.daniloscataloni.liftking.data.repositories

import com.daniloscataloni.liftking.data.daos.WorkoutDao
import com.daniloscataloni.liftking.data.daos.WorkoutExerciseDao
import com.daniloscataloni.liftking.data.mappers.toDomain
import com.daniloscataloni.liftking.data.mappers.toEntity
import com.daniloscataloni.liftking.domain.models.Workout
import com.daniloscataloni.liftking.domain.models.WorkoutExercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface IWorkoutRepository {
    // Workout operations
    fun getWorkoutsByPeriodization(periodizationId: Long): Flow<List<Workout>>
    suspend fun getWorkoutById(id: Long): Workout?
    suspend fun insertWorkout(workout: Workout): Long
    suspend fun updateWorkout(workout: Workout)
    suspend fun deleteWorkout(workout: Workout)

    // WorkoutExercise operations
    fun getExercisesForWorkout(workoutId: Long): Flow<List<WorkoutExercise>>
    suspend fun addExerciseToWorkout(workoutExercise: WorkoutExercise): Long
    suspend fun updateWorkoutExercise(workoutExercise: WorkoutExercise)
    suspend fun removeExerciseFromWorkout(workoutExercise: WorkoutExercise)
    suspend fun removeExerciseFromWorkout(workoutId: Long, exerciseId: Int)
    suspend fun reorderExercises(workoutExercises: List<WorkoutExercise>)

    class WorkoutRepository(
        private val workoutDao: WorkoutDao,
        private val workoutExerciseDao: WorkoutExerciseDao
    ) : IWorkoutRepository {

        // --- Workout operations ---

        override fun getWorkoutsByPeriodization(periodizationId: Long): Flow<List<Workout>> {
            return workoutDao.getByPeriodization(periodizationId).map { entities ->
                entities.map { it.toDomain() }
            }
        }

        override suspend fun getWorkoutById(id: Long): Workout? {
            return workoutDao.getById(id)?.toDomain()
        }

        override suspend fun insertWorkout(workout: Workout): Long {
            return workoutDao.insert(workout.toEntity())
        }

        override suspend fun updateWorkout(workout: Workout) {
            workoutDao.update(workout.toEntity())
        }

        override suspend fun deleteWorkout(workout: Workout) {
            workoutDao.delete(workout.toEntity())
        }

        // --- WorkoutExercise operations ---

        override fun getExercisesForWorkout(workoutId: Long): Flow<List<WorkoutExercise>> {
            return workoutExerciseDao.getByWorkout(workoutId).map { entities ->
                entities.map { it.toDomain() }
            }
        }

        override suspend fun addExerciseToWorkout(workoutExercise: WorkoutExercise): Long {
            return workoutExerciseDao.insert(workoutExercise.toEntity())
        }

        override suspend fun updateWorkoutExercise(workoutExercise: WorkoutExercise) {
            workoutExerciseDao.update(workoutExercise.toEntity())
        }

        override suspend fun removeExerciseFromWorkout(workoutExercise: WorkoutExercise) {
            workoutExerciseDao.delete(workoutExercise.toEntity())
        }

        override suspend fun removeExerciseFromWorkout(workoutId: Long, exerciseId: Int) {
            workoutExerciseDao.removeExerciseFromWorkout(workoutId, exerciseId)
        }

        override suspend fun reorderExercises(workoutExercises: List<WorkoutExercise>) {
            workoutExercises.forEach { exercise ->
                workoutExerciseDao.updateOrder(exercise.id, exercise.order)
            }
        }
    }
}
