package com.daniloscataloni.liftking

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.daniloscataloni.liftking.navigation.LiftKingNavHost
import com.daniloscataloni.liftking.ui.theme.LiftKingTheme

class MainActivity : ComponentActivity() {

    private var pendingTrainingWorkoutId by mutableStateOf<Long?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pendingTrainingWorkoutId = intent.extractPendingTrainingWorkoutId()
        setContent {
            LiftKingTheme {
                LiftKingNavHost(
                    navController = rememberNavController(),
                    pendingTrainingWorkoutId = pendingTrainingWorkoutId,
                    onPendingTrainingWorkoutConsumed = { pendingTrainingWorkoutId = null }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingTrainingWorkoutId = intent.extractPendingTrainingWorkoutId()
    }

    companion object {
        const val EXTRA_OPEN_TRAINING_WORKOUT_ID = "extra_open_training_workout_id"

        fun createOpenTrainingPendingIntent(context: Context, workoutId: Long): PendingIntent {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(EXTRA_OPEN_TRAINING_WORKOUT_ID, workoutId)
            }

            return PendingIntent.getActivity(
                context,
                workoutId.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}

private fun Intent?.extractPendingTrainingWorkoutId(): Long? {
    val workoutId = this?.getLongExtra(MainActivity.EXTRA_OPEN_TRAINING_WORKOUT_ID, -1L) ?: -1L
    return workoutId.takeIf { it > 0L }
}
