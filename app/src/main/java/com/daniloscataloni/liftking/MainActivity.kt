package com.daniloscataloni.liftking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.daniloscataloni.liftking.ui.screens.ExerciseListScreen
import com.daniloscataloni.liftking.ui.theme.LiftKingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiftKingTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "exercise_list") {
                    composable("exercise_list") {
                        ExerciseListScreen()
                    }
                }
            }
        }
    }
}