package com.daniloscataloni.liftking.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.daniloscataloni.liftking.data.entities.PeriodizationEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO = Data Access Object
 *
 * Interface que define todas as operações de banco para Periodization.
 * O Room gera a implementação automaticamente em tempo de compilação.
 *
 * Padrões importantes:
 * - suspend fun = operação assíncrona (usa coroutines)
 * - Flow<T> = stream reativo (UI atualiza automaticamente quando dados mudam)
 */
@Dao
interface PeriodizationDao {

    // ========== OPERAÇÕES BÁSICAS (CRUD) ==========

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(periodization: PeriodizationEntity): Long

    @Update
    suspend fun update(periodization: PeriodizationEntity)

    @Delete
    suspend fun delete(periodization: PeriodizationEntity)

    // ========== QUERIES DE LEITURA ==========

    /**
     * Retorna todas as periodizações não arquivadas, ordenadas por data.
     * Flow permite que a UI reaja automaticamente a mudanças.
     */
    @Query("SELECT * FROM periodizations WHERE isArchived = 0 ORDER BY createdAt DESC")
    fun getAllActive(): Flow<List<PeriodizationEntity>>

    /**
     * Retorna apenas periodizações arquivadas.
     */
    @Query("SELECT * FROM periodizations WHERE isArchived = 1 ORDER BY createdAt DESC")
    fun getAllArchived(): Flow<List<PeriodizationEntity>>

    /**
     * Busca a periodização que está marcada como ativa.
     * Pode retornar null se nenhuma estiver ativa.
     */
    @Query("SELECT * FROM periodizations WHERE isActive = 1 LIMIT 1")
    suspend fun getActive(): PeriodizationEntity?

    /**
     * Versão reativa do getActive - útil para observar mudanças.
     */
    @Query("SELECT * FROM periodizations WHERE isActive = 1 LIMIT 1")
    fun observeActive(): Flow<PeriodizationEntity?>

    @Query("SELECT * FROM periodizations WHERE id = :id")
    suspend fun getById(id: Long): PeriodizationEntity?

    // ========== OPERAÇÕES ESPECIAIS ==========

    /**
     * Desativa todas as periodizações.
     * Chamado antes de ativar uma nova (só pode ter uma ativa).
     */
    @Query("UPDATE periodizations SET isActive = 0 WHERE isActive = 1")
    suspend fun deactivateAll()

    /**
     * Ativa uma periodização específica.
     * Use junto com deactivateAll() em uma transação.
     */
    @Query("UPDATE periodizations SET isActive = 1 WHERE id = :id")
    suspend fun activate(id: Long)

    /**
     * Transação que garante atomicidade: desativa todas E ativa a escolhida.
     * Se algo falhar, nenhuma mudança é aplicada.
     */
    @Transaction
    suspend fun setActive(id: Long) {
        deactivateAll()
        activate(id)
    }

    /**
     * Arquiva uma periodização (soft delete).
     * Mantém os dados mas remove da lista principal.
     */
    @Query("UPDATE periodizations SET isArchived = 1, isActive = 0 WHERE id = :id")
    suspend fun archive(id: Long)

    /**
     * Restaura uma periodização arquivada.
     */
    @Query("UPDATE periodizations SET isArchived = 0 WHERE id = :id")
    suspend fun unarchive(id: Long)
}
