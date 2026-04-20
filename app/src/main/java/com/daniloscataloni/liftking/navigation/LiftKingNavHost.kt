package com.daniloscataloni.liftking.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.daniloscataloni.liftking.R
import com.daniloscataloni.liftking.ui.screens.ExercisesScreen
import com.daniloscataloni.liftking.ui.screens.PeriodizationScreen
import com.daniloscataloni.liftking.ui.screens.TrainingScreen
import com.daniloscataloni.liftking.ui.screens.WorkoutListScreen
import com.daniloscataloni.liftking.ui.theme.BackgroundGray
import com.daniloscataloni.liftking.ui.theme.DeepBlack
import com.daniloscataloni.liftking.ui.theme.SmoothGray

private enum class TopLevelDestination(
    val route: Route,
    val labelRes: Int,
    val icon: @Composable () -> Unit
) {
    Workouts(
        route = Route.Periodizations,
        labelRes = R.string.nav_workouts,
        icon = { Icon(Icons.Outlined.Home, contentDescription = null) }
    ),
    Exercises(
        route = Route.Exercises,
        labelRes = R.string.nav_exercises,
        icon = { Icon(Icons.Outlined.FitnessCenter, contentDescription = null) }
    )
}

@Suppress("LongMethod")
@Composable
fun LiftKingNavHost(
    navController: NavHostController = rememberNavController(),
    pendingTrainingWorkoutId: Long? = null,
    onPendingTrainingWorkoutConsumed: () -> Unit = {}
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val selectedTopLevelDestination = if (currentDestination?.hasRoute<Route.Exercises>() == true) {
        TopLevelDestination.Exercises
    } else {
        TopLevelDestination.Workouts
    }

    LaunchedEffect(pendingTrainingWorkoutId) {
        pendingTrainingWorkoutId ?: return@LaunchedEffect
        navController.navigateToTraining(
            workoutId = pendingTrainingWorkoutId,
            launchSingleTop = true
        )
        onPendingTrainingWorkoutConsumed()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = BackgroundGray,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TopLevelDestination.entries.forEach { destination ->
                        val isSelected = selectedTopLevelDestination == destination
                        val contentColor = if (isSelected) {
                            MaterialTheme.colorScheme.onBackground
                        } else {
                            SmoothGray
                        }

                        Surface(
                            color = if (isSelected) DeepBlack else BackgroundGray,
                            shape = RoundedCornerShape(22.dp),
                            modifier = Modifier.clickable {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                androidx.compose.runtime.CompositionLocalProvider(
                                    androidx.compose.material3.LocalContentColor provides contentColor
                                ) {
                                    destination.icon()
                                }
                                Text(
                                    text = stringResource(destination.labelRes),
                                    color = contentColor,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Route.Periodizations,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable<Route.Periodizations> {
                PeriodizationScreen(
                    onPeriodizationSelected = { periodization ->
                        navController.navigateToWorkouts(periodization.id)
                    }
                )
            }

            composable<Route.Workouts> { backStackEntryForRoute ->
                val route: Route.Workouts = backStackEntryForRoute.toRoute()
                WorkoutListScreen(
                    periodizationId = route.periodizationId,
                    onBackClick = { navController.popBackStackSafely() },
                    onWorkoutClick = { workout ->
                        navController.navigateToTraining(workout.id)
                    }
                )
            }

            composable<Route.Training> { backStackEntryForRoute ->
                val route: Route.Training = backStackEntryForRoute.toRoute()
                TrainingScreen(
                    workoutId = route.workoutId,
                    onBackClick = { navController.popBackStackSafely() },
                    onComplete = { navController.popBackStackSafely() }
                )
            }

            composable<Route.Exercises> {
                ExercisesScreen()
            }
        }
    }
}
