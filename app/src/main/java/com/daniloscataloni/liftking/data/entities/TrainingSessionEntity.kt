package com.daniloscataloni.liftking.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa uma sessão de treino - quando o usuário vai à academia.
 *
 * Cada vez que você faz o "Treino A", cria uma nova TrainingSession.
 * Isso permite:
 * - Ver histórico de treinos
 * - Comparar sessões ao longo do tempo
 * - Mostrar "último treino" na tela de registro
 *
 * Relacionamento: Workout (1) → (N) TrainingSession
 */
@Entity(
    tableName = "training_sessions",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutEntity::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["workoutId"]),
        Index(value = ["date"]) // Para buscar por data
    ]
)
data class TrainingSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val workoutId: Long,

    /**
     * Data do treino em milissegundos (epoch time).
     * Usamos Long para facilitar queries de range (treinos entre data X e Y).
     */
    val date: Long = System.currentTimeMillis(),

    /**
     * Notas opcionais sobre a sessão.
     * Ex: "Não dormi bem", "Testando nova técnica", etc.
     */
    val notes: String? = null,

    /**
     * Indica se o treino foi finalizado.
     * Útil para diferenciar treinos em andamento de completos.
     */
    val isCompleted: Boolean = false
)
