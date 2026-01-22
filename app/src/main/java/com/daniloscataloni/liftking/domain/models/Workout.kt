package com.daniloscataloni.liftking.domain.models

/**
 * Domain model para um Treino dentro de uma periodização.
 * Ex: "Treino A - Peito e Tríceps"
 */
data class Workout(
    val id: Long = 0,
    val periodizationId: Long,
    val name: String,
    val order: Int = 0
)

/**
 * Associação entre Workout e Exercise com metadados.
 * Armazena quantas séries são o target para esse exercício nesse treino.
 */
data class WorkoutExercise(
    val id: Long = 0,
    val workoutId: Long,
    val exerciseId: Int,
    val order: Int = 0,
    val targetSets: Int? = null
)