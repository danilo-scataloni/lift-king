package com.daniloscataloni.liftking.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Registro de uma série individual.
 *
 * Este é o dado mais granular: cada linha que você preenche no app.
 * Ex: Série 1 do Agachamento: 140kg x 5 reps x RIR 2
 *
 * Relacionamento: ExerciseLog (1) → (N) SetLog
 */
@Entity(
    tableName = "set_logs",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseLogEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseLogId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["exerciseLogId"])]
)
data class SetLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val exerciseLogId: Long,

    /**
     * Número da série (1, 2, 3...).
     * Permite ordenar e identificar cada série.
     */
    val setNumber: Int,

    /**
     * Peso em kg.
     * Usamos Float para permitir valores como 52.5kg.
     */
    val weight: Float,

    /**
     * Número de repetições completadas.
     */
    val reps: Int,

    /**
     * RIR = Reps In Reserve (Repetições em Reserva).
     * Quantas reps você poderia ter feito antes da falha.
     * - RIR 0 = falha total
     * - RIR 1 = sobrou 1 rep
     * - RIR 2 = sobrou 2 reps
     */
    val rir: Int? = null
)
