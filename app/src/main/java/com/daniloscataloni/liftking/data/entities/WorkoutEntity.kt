package com.daniloscataloni.liftking.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa um treino dentro de uma periodização (ex: "Treino A - Squat Focus").
 *
 * Relacionamento: Periodization (1) → (N) Workout
 * - Uma periodização tem vários treinos
 * - Um treino pertence a uma única periodização
 */
@Entity(
    tableName = "workouts",
    foreignKeys = [
        ForeignKey(
            entity = PeriodizationEntity::class,
            parentColumns = ["id"],
            childColumns = ["periodizationId"],
            // CASCADE: Se deletar a periodização, deleta os treinos também
            onDelete = ForeignKey.CASCADE
        )
    ],
    // Index melhora performance de queries que filtram por periodizationId
    indices = [Index(value = ["periodizationId"])]
)
data class WorkoutEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /**
     * Referência à periodização pai.
     * ForeignKey garante que este ID existe na tabela periodizations.
     */
    val periodizationId: Long,

    val name: String,

    /**
     * Ordem de exibição na lista de treinos (0, 1, 2...).
     * Permite reordenar treinos sem alterar IDs.
     */
    val order: Int = 0
)
