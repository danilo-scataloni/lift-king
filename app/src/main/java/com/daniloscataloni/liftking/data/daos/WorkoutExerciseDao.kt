package com.daniloscataloni.liftking.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.daniloscataloni.liftking.data.entities.WorkoutExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutExerciseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workoutExercise: WorkoutExerciseEntity): Long

    @Update
    suspend fun update(workoutExercise: WorkoutExerciseEntity)

    @Delete
    suspend fun delete(workoutExercise: WorkoutExerciseEntity)

    /**
     * Busca todos os exercícios de um treino, ordenados.
     */
    @Query("SELECT * FROM workout_exercises WHERE workoutId = :workoutId ORDER BY `order` ASC")
    fun getByWorkout(workoutId: Long): Flow<List<WorkoutExerciseEntity>>

    /**
     * Versão não-reativa para uso pontual.
     */
    @Query("SELECT * FROM workout_exercises WHERE workoutId = :workoutId ORDER BY `order` ASC")
    suspend fun getByWorkoutOnce(workoutId: Long): List<WorkoutExerciseEntity>

    @Query("SELECT * FROM workout_exercises WHERE id = :id")
    suspend fun getById(id: Long): WorkoutExerciseEntity?

    @Query("UPDATE workout_exercises SET `order` = :newOrder WHERE id = :id")
    suspend fun updateOrder(id: Long, newOrder: Int)

    @Query("SELECT COUNT(*) FROM workout_exercises WHERE workoutId = :workoutId")
    suspend fun countByWorkout(workoutId: Long): Int

    /**
     * Remove um exercício de um treino específico.
     */
    @Query("DELETE FROM workout_exercises WHERE workoutId = :workoutId AND exerciseId = :exerciseId")
    suspend fun removeExerciseFromWorkout(workoutId: Long, exerciseId: Int)

    @Query("DELETE FROM workout_exercises WHERE id = :id")
    suspend fun deleteWorkoutExerciseById(id: Long)
}
