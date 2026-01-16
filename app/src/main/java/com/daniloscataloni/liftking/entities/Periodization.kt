package com.daniloscataloni.liftking.entities

/**
 * Domain model para Periodização.
 * Representa um ciclo de treino (ex: "Hipertrofia Jan-Mar 2024")
 */
data class Periodization(
    val id: Long = 0,
    val name: String,
    val isActive: Boolean = false,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)