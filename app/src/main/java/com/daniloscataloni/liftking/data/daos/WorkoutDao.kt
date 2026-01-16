package com.daniloscataloni.liftking.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.daniloscataloni.liftking.data.entities.WorkoutEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: WorkoutEntity): Long

    @Update
    suspend fun update(workout: WorkoutEntity)

    @Delete
    suspend fun delete(workout: WorkoutEntity)

    /**
     * Busca todos os treinos de uma periodização, ordenados.
     */
    @Query("SELECT * FROM workouts WHERE periodizationId = :periodizationId ORDER BY `order` ASC")
    fun getByPeriodization(periodizationId: Long): Flow<List<WorkoutEntity>>

    @Query("SELECT * FROM workouts WHERE id = :id")
    suspend fun getById(id: Long): WorkoutEntity?

    /**
     * Atualiza a ordem de um treino.
     * Útil para reordenação via drag & drop.
     */
    @Query("UPDATE workouts SET `order` = :newOrder WHERE id = :id")
    suspend fun updateOrder(id: Long, newOrder: Int)

    /**
     * Conta quantos treinos existem em uma periodização.
     * Útil para definir a ordem do próximo treino a ser criado.
     */
    @Query("SELECT COUNT(*) FROM workouts WHERE periodizationId = :periodizationId")
    suspend fun countByPeriodization(periodizationId: Long): Int
}
