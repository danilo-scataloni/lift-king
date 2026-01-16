package com.daniloscataloni.liftking.repositories

import com.daniloscataloni.liftking.data.daos.PeriodizationDao
import com.daniloscataloni.liftking.data.entities.PeriodizationEntity
import com.daniloscataloni.liftking.entities.Periodization
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface IPeriodizationRepository {
    fun getAllPeriodizations(): Flow<List<Periodization>>
    fun getActivePeriodizations(): Flow<List<Periodization>>
    fun getActivePeriodization(): Flow<Periodization?>
    suspend fun insert(periodization: Periodization): Long
    suspend fun update(periodization: Periodization)
    suspend fun delete(periodization: Periodization)
    suspend fun setActive(id: Long)
    suspend fun archive(id: Long)
    suspend fun unarchive(id: Long)
}

class PeriodizationRepository(
    private val periodizationDao: PeriodizationDao
) : IPeriodizationRepository {

    override fun getAllPeriodizations(): Flow<List<Periodization>> {
        return periodizationDao.getAllActive().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getActivePeriodizations(): Flow<List<Periodization>> {
        return periodizationDao.getAllActive().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getActivePeriodization(): Flow<Periodization?> {
        return periodizationDao.observeActive().map { it?.toDomain() }
    }

    override suspend fun insert(periodization: Periodization): Long {
        return periodizationDao.insert(periodization.toEntity())
    }

    override suspend fun update(periodization: Periodization) {
        periodizationDao.update(periodization.toEntity())
    }

    override suspend fun delete(periodization: Periodization) {
        periodizationDao.delete(periodization.toEntity())
    }

    override suspend fun setActive(id: Long) {
        periodizationDao.setActive(id)
    }

    override suspend fun archive(id: Long) {
        periodizationDao.archive(id)
    }

    override suspend fun unarchive(id: Long) {
        periodizationDao.unarchive(id)
    }
}

// Extensões para conversão Entity <-> Domain
private fun PeriodizationEntity.toDomain(): Periodization {
    return Periodization(
        id = this.id,
        name = this.name,
        isActive = this.isActive,
        isArchived = this.isArchived,
        createdAt = this.createdAt
    )
}

private fun Periodization.toEntity(): PeriodizationEntity {
    return PeriodizationEntity(
        id = this.id,
        name = this.name,
        isActive = this.isActive,
        isArchived = this.isArchived,
        createdAt = this.createdAt
    )
}
