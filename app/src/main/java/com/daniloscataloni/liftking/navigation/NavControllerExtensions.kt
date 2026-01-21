package com.daniloscataloni.liftking.navigation

import androidx.navigation.NavController

fun NavController.navigateToWorkouts(periodizationId: Long) {
    navigate(Route.Workouts(periodizationId))
}

fun NavController.navigateToTraining(workoutId: Long) {
    navigate(Route.Training(workoutId))
}

fun NavController.navigateToExerciseList() {
    navigate(Route.ExerciseList)
}
