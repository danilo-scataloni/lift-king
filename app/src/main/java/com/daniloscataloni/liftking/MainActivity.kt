package com.daniloscataloni.liftking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.daniloscataloni.liftking.ui.screens.ExerciseListScreen
import com.daniloscataloni.liftking.ui.screens.PeriodizationScreen
import com.daniloscataloni.liftking.ui.screens.TrainingScreen
import com.daniloscataloni.liftking.ui.screens.WorkoutListScreen
import com.daniloscataloni.liftking.ui.theme.LiftKingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiftKingTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "periodizations") {
                    composable("periodizations") {
                        PeriodizationScreen(
                            onPeriodizationSelected = { periodization ->
                                navController.navigate("workouts/${periodization.id}")
                            }
                        )
                    }
                    composable(
                        route = "workouts/{periodizationId}",
                        arguments = listOf(navArgument("periodizationId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val periodizationId = backStackEntry.arguments?.getLong("periodizationId") ?: 0L
                        WorkoutListScreen(
                            periodizationId = periodizationId,
                            onBackClick = { navController.popBackStack() },
                            onWorkoutClick = { workout ->
                                navController.navigate("training/${workout.id}")
                            }
                        )
                    }
                    composable(
                        route = "training/{workoutId}",
                        arguments = listOf(navArgument("workoutId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val workoutId = backStackEntry.arguments?.getLong("workoutId") ?: 0L
                        TrainingScreen(
                            workoutId = workoutId,
                            onBackClick = { navController.popBackStack() },
                            onComplete = { navController.popBackStack() }
                        )
                    }
                    composable("exercise_list") {
                        ExerciseListScreen()
                    }
                }
            }
        }
    }
}
