package com.daniloscataloni.liftking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.daniloscataloni.liftking.navigation.LiftKingNavHost
import com.daniloscataloni.liftking.ui.theme.LiftKingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiftKingTheme {
                LiftKingNavHost(rememberNavController())
            }
        }
    }
}
