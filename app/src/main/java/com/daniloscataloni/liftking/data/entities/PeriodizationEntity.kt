package com.daniloscataloni.liftking.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa uma periodização de treinos (ex: "Preparação Competição 2024").
 *
 * Uma periodização agrupa vários treinos (Workout) e pode ser:
 * - Ativa: é a periodização atual sendo usada
 * - Arquivada: periodização antiga que foi substituída
 *
 * Regra de negócio: Apenas UMA periodização pode estar ativa por vez.
 */
@Entity(tableName = "periodizations")
data class PeriodizationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    /**
     * Indica se esta é a periodização ativa.
     * Apenas uma periodização deve ter isActive = true.
     */
    val isActive: Boolean = false,

    /**
     * Periodizações arquivadas não aparecem na lista principal,
     * mas mantêm o histórico de treinos.
     */
    val isArchived: Boolean = false,

    /**
     * Timestamp de criação para ordenação.
     * Usamos Long (milissegundos) pois Room não suporta LocalDateTime nativamente.
     */
    val createdAt: Long = System.currentTimeMillis()
)
