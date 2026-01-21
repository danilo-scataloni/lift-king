package com.daniloscataloni.liftking.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.daniloscataloni.liftking.ui.screens.ExerciseListScreen
import com.daniloscataloni.liftking.ui.screens.PeriodizationScreen
import com.daniloscataloni.liftking.ui.screens.TrainingScreen
import com.daniloscataloni.liftking.ui.screens.WorkoutListScreen

@Composable
fun LiftKingNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Route.Periodizations
    ) {
        composable<Route.Periodizations> {
            PeriodizationScreen(
                onPeriodizationSelected = { periodization ->
                    navController.navigateToWorkouts(periodization.id)
                }
            )
        }

        composable<Route.Workouts> { backStackEntry ->
            val route: Route.Workouts = backStackEntry.toRoute()
            WorkoutListScreen(
                periodizationId = route.periodizationId,
                onBackClick = { navController.popBackStack() },
                onWorkoutClick = { workout ->
                    navController.navigateToTraining(workout.id)
                }
            )
        }

        composable<Route.Training> { backStackEntry ->
            val route: Route.Training = backStackEntry.toRoute()
            TrainingScreen(
                workoutId = route.workoutId,
                onBackClick = { navController.popBackStack() },
                onComplete = { navController.popBackStack() }
            )
        }

        composable<Route.ExerciseList> {
            ExerciseListScreen()
        }
    }
}
