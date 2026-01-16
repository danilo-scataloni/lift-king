package com.daniloscataloni.liftking.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.daniloscataloni.liftking.data.entities.ExerciseLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exerciseLog: ExerciseLogEntity): Long

    @Update
    suspend fun update(exerciseLog: ExerciseLogEntity)

    @Delete
    suspend fun delete(exerciseLog: ExerciseLogEntity)

    @Query("SELECT * FROM exercise_logs WHERE id = :id")
    suspend fun getById(id: Long): ExerciseLogEntity?

    /**
     * Busca todos os logs de exercício de uma sessão.
     */
    @Query("SELECT * FROM exercise_logs WHERE sessionId = :sessionId ORDER BY `order` ASC")
    fun getBySession(sessionId: Long): Flow<List<ExerciseLogEntity>>

    /**
     * Versão não-reativa.
     */
    @Query("SELECT * FROM exercise_logs WHERE sessionId = :sessionId ORDER BY `order` ASC")
    suspend fun getBySessionOnce(sessionId: Long): List<ExerciseLogEntity>

    /**
     * Busca o último log de um exercício específico em qualquer sessão.
     * Usado para mostrar "último treino" daquele exercício.
     */
    @Query("""
        SELECT el.* FROM exercise_logs el
        INNER JOIN training_sessions ts ON el.sessionId = ts.id
        WHERE el.exerciseId = :exerciseId AND ts.isCompleted = 1
        ORDER BY ts.date DESC
        LIMIT 1
    """)
    suspend fun getLastLogForExercise(exerciseId: Int): ExerciseLogEntity?

    /**
     * Busca o último log de um exercício dentro de um treino específico.
     * Mais específico: "última vez que fiz Agachamento no Treino A".
     */
    @Query("""
        SELECT el.* FROM exercise_logs el
        INNER JOIN training_sessions ts ON el.sessionId = ts.id
        WHERE el.exerciseId = :exerciseId
            AND ts.workoutId = :workoutId
            AND ts.isCompleted = 1
        ORDER BY ts.date DESC
        LIMIT 1
    """)
    suspend fun getLastLogForExerciseInWorkout(exerciseId: Int, workoutId: Long): ExerciseLogEntity?
}
