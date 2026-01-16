package com.daniloscataloni.liftking.repositories

import com.daniloscataloni.liftking.data.daos.WorkoutDao
import com.daniloscataloni.liftking.data.daos.WorkoutExerciseDao
import com.daniloscataloni.liftking.data.entities.WorkoutEntity
import com.daniloscataloni.liftking.data.entities.WorkoutExerciseEntity
import com.daniloscataloni.liftking.entities.Workout
import com.daniloscataloni.liftking.entities.WorkoutExercise
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
    suspend fun reorderExercises(workoutExercises: List<WorkoutExercise>)
}

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

    override suspend fun reorderExercises(workoutExercises: List<WorkoutExercise>) {
        workoutExercises.forEach { exercise ->
            workoutExerciseDao.updateOrder(exercise.id, exercise.order)
        }
    }
}

// --- Extensões de conversão ---

private fun WorkoutEntity.toDomain(): Workout {
    return Workout(
        id = this.id,
        periodizationId = this.periodizationId,
        name = this.name,
        order = this.order
    )
}

private fun Workout.toEntity(): WorkoutEntity {
    return WorkoutEntity(
        id = this.id,
        periodizationId = this.periodizationId,
        name = this.name,
        order = this.order
    )
}

private fun WorkoutExerciseEntity.toDomain(): WorkoutExercise {
    return WorkoutExercise(
        id = this.id,
        workoutId = this.workoutId,
        exerciseId = this.exerciseId,
        order = this.order,
        targetSets = this.targetSets
    )
}

private fun WorkoutExercise.toEntity(): WorkoutExerciseEntity {
    return WorkoutExerciseEntity(
        id = this.id,
        workoutId = this.workoutId,
        exerciseId = this.exerciseId,
        order = this.order,
        targetSets = this.targetSets
    )
}
