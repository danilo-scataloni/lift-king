package com.daniloscataloni.liftking.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.daniloscataloni.liftking.data.entities.SetLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SetLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(setLog: SetLogEntity): Long

    /**
     * Insere múltiplas séries de uma vez.
     * Útil ao salvar todas as séries de um exercício.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(setLogs: List<SetLogEntity>): List<Long>

    @Update
    suspend fun update(setLog: SetLogEntity)

    @Delete
    suspend fun delete(setLog: SetLogEntity)

    @Query("SELECT * FROM set_logs WHERE id = :id")
    suspend fun getById(id: Long): SetLogEntity?

    /**
     * Busca todas as séries de um log de exercício.
     */
    @Query("SELECT * FROM set_logs WHERE exerciseLogId = :exerciseLogId ORDER BY setNumber ASC")
    fun getByExerciseLog(exerciseLogId: Long): Flow<List<SetLogEntity>>

    /**
     * Versão não-reativa.
     */
    @Query("SELECT * FROM set_logs WHERE exerciseLogId = :exerciseLogId ORDER BY setNumber ASC")
    suspend fun getByExerciseLogOnce(exerciseLogId: Long): List<SetLogEntity>

    /**
     * Busca as séries do último treino completado de um exercício em um workout específico.
     * Usa subquery para pegar apenas a sessão mais recente (por data).
     */
    @Query("""
        SELECT sl.* FROM set_logs sl
        INNER JOIN exercise_logs el ON sl.exerciseLogId = el.id
        WHERE el.exerciseId = :exerciseId
          AND el.sessionId = (
              SELECT ts.id FROM training_sessions ts
              WHERE ts.workoutId = :workoutId
                AND ts.isCompleted = 1
              ORDER BY ts.date DESC
              LIMIT 1
          )
        ORDER BY sl.setNumber ASC
    """)
    suspend fun getLastSetsForExerciseInWorkout(exerciseId: Int, workoutId: Long): List<SetLogEntity>

    /**
     * Deleta todas as séries de um log de exercício.
     * Útil para resetar e inserir novamente.
     */
    @Query("DELETE FROM set_logs WHERE exerciseLogId = :exerciseLogId")
    suspend fun deleteByExerciseLog(exerciseLogId: Long)

    /**
     * Busca o maior peso já levantado em um exercício (PR - Personal Record).
     */
    @Query("""
        SELECT MAX(weight) FROM set_logs sl
        INNER JOIN exercise_logs el ON sl.exerciseLogId = el.id
        WHERE el.exerciseId = :exerciseId
    """)
    suspend fun getMaxWeightForExercise(exerciseId: Int): Float?

    /**
     * Conta total de séries feitas de um exercício.
     * Útil para estatísticas.
     */
    @Query("""
        SELECT COUNT(*) FROM set_logs sl
        INNER JOIN exercise_logs el ON sl.exerciseLogId = el.id
        WHERE el.exerciseId = :exerciseId
    """)
    suspend fun countSetsForExercise(exerciseId: Int): Int
}
