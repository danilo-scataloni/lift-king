package com.daniloscataloni.liftking.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Registro de um exercício dentro de uma sessão de treino.
 *
 * Agrupa todas as séries de um exercício em uma sessão.
 * Ex: Na sessão de 14/01, fiz Agachamento → isso é um ExerciseLog.
 *
 * Relacionamentos:
 * - TrainingSession (1) → (N) ExerciseLog
 * - Exercise (1) → (N) ExerciseLog
 */
@Entity(
    tableName = "exercise_logs",
    foreignKeys = [
        ForeignKey(
            entity = TrainingSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["sessionId"]),
        Index(value = ["exerciseId"]),
        // Índice composto para buscar "último log deste exercício"
        Index(value = ["exerciseId", "sessionId"])
    ]
)
data class ExerciseLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val sessionId: Long,
    val exerciseId: Int,

    /**
     * Ordem em que o exercício foi feito na sessão.
     * Pode diferir da ordem planejada no WorkoutExercise.
     */
    val order: Int = 0,

    /**
     * Notas específicas do exercício nesta sessão.
     * Ex: "Dor no ombro", "Trocar grip próxima vez"
     */
    val notes: String? = null
)
