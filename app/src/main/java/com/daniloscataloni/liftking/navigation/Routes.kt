package com.daniloscataloni.liftking.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    @Serializable
    data object Periodizations : Route

    @Serializable
    data class Workouts(val periodizationId: Long) : Route

    @Serializable
    data class Training(val workoutId: Long) : Route
}
