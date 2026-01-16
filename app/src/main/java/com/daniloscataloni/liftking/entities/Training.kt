package com.daniloscataloni.liftking.entities

/**
 * Domain model para uma Sessão de Treino.
 * Representa um dia específico em que o usuário treinou.
 */
data class TrainingSession(
    val id: Long = 0,
    val workoutId: Long,
    val date: Long = System.currentTimeMillis(),
    val notes: String? = null,
    val isCompleted: Boolean = false
)

/**
 * Domain model para o log de um exercício em uma sessão.
 * Agrupa todas as séries feitas de um exercício específico.
 */
data class ExerciseLog(
    val id: Long = 0,
    val sessionId: Long,
    val exerciseId: Int,
    val notes: String? = null
)

/**
 * Domain model para uma série individual.
 * Contém peso, repetições e RIR.
 */
data class SetLog(
    val id: Long = 0,
    val exerciseLogId: Long,
    val setNumber: Int,
    val weight: Float,
    val reps: Int,
    val rir: Int? = null
)
