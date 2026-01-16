package com.daniloscataloni.liftking.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.daniloscataloni.liftking.data.entities.TrainingSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: TrainingSessionEntity): Long

    @Update
    suspend fun update(session: TrainingSessionEntity)

    @Delete
    suspend fun delete(session: TrainingSessionEntity)

    @Query("SELECT * FROM training_sessions WHERE id = :id")
    suspend fun getById(id: Long): TrainingSessionEntity?

    /**
     * Busca a última sessão de um treino específico.
     * Usado para mostrar "último treino" na tela de registro.
     */
    @Query("""
        SELECT * FROM training_sessions
        WHERE workoutId = :workoutId AND isCompleted = 1
        ORDER BY date DESC
        LIMIT 1
    """)
    suspend fun getLastSessionForWorkout(workoutId: Long): TrainingSessionEntity?

    /**
     * Busca todas as sessões de um treino, ordenadas por data.
     * Útil para histórico.
     */
    @Query("SELECT * FROM training_sessions WHERE workoutId = :workoutId ORDER BY date DESC")
    fun getSessionsByWorkout(workoutId: Long): Flow<List<TrainingSessionEntity>>

    /**
     * Busca sessões em um intervalo de datas.
     * Útil para filtros de histórico e relatórios.
     */
    @Query("""
        SELECT * FROM training_sessions
        WHERE date >= :startDate AND date <= :endDate
        ORDER BY date DESC
    """)
    fun getSessionsByDateRange(startDate: Long, endDate: Long): Flow<List<TrainingSessionEntity>>

    /**
     * Busca todas as sessões (para histórico geral).
     */
    @Query("SELECT * FROM training_sessions ORDER BY date DESC")
    fun getAllSessions(): Flow<List<TrainingSessionEntity>>

    /**
     * Marca uma sessão como completa.
     */
    @Query("UPDATE training_sessions SET isCompleted = 1 WHERE id = :sessionId")
    suspend fun markAsCompleted(sessionId: Long)

    /**
     * Busca sessão em andamento (não completada) de um treino.
     * Útil para continuar treino pausado.
     */
    @Query("""
        SELECT * FROM training_sessions
        WHERE workoutId = :workoutId AND isCompleted = 0
        ORDER BY date DESC
        LIMIT 1
    """)
    suspend fun getInProgressSession(workoutId: Long): TrainingSessionEntity?
}
