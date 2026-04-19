package com.daniloscataloni.liftking.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute

fun NavController.navigateToWorkouts(periodizationId: Long) {
    navigate(Route.Workouts(periodizationId))
}

fun NavController.navigateToTraining(workoutId: Long) {
    navigate(Route.Training(workoutId))
}

fun NavController.popBackStackSafely(): Boolean {
    val currentDestination = currentDestination ?: return false
    val isAtTopLevelDestination = currentDestination.hasRoute<Route.Periodizations>() ||
        currentDestination.hasRoute<Route.Exercises>()

    if (isAtTopLevelDestination) return false

    return popBackStack()
}
