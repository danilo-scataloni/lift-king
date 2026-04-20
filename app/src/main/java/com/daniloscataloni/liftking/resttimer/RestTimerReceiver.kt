package com.daniloscataloni.liftking.resttimer

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.daniloscataloni.liftking.domain.models.RestTimer

class RestTimerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        RestTimerStorage(context).clearActiveTimer()
        val notificationManager = RestNotificationManager(context)
        notificationManager.cancelOngoingRestTimerNotification()
        val isAppInForeground = ProcessLifecycleOwner.get()
            .lifecycle
            .currentState
            .isAtLeast(Lifecycle.State.STARTED)

        if (isAppInForeground) {
            vibrateForRest(context)
            return
        }

        notificationManager.showRestTimerFinishedNotification(
            workoutId = intent.getLongExtra(EXTRA_WORKOUT_ID, 0L),
            workoutName = intent.getStringExtra(EXTRA_WORKOUT_NAME).orEmpty(),
            exerciseName = intent.getStringExtra(EXTRA_EXERCISE_NAME).orEmpty()
        )
    }

    companion object {
        private const val REQUEST_CODE = 9911
        private const val ACTION_REST_TIMER_FINISHED =
            "com.daniloscataloni.liftking.action.REST_TIMER_FINISHED"
        private const val EXTRA_WORKOUT_ID = "extra_workout_id"
        private const val EXTRA_WORKOUT_NAME = "extra_workout_name"
        private const val EXTRA_EXERCISE_NAME = "extra_exercise_name"

        fun createPendingIntent(context: Context, restTimer: RestTimer? = null): PendingIntent {
            val intent = Intent(context, RestTimerReceiver::class.java).apply {
                action = ACTION_REST_TIMER_FINISHED
                if (restTimer != null) {
                    putExtra(EXTRA_WORKOUT_ID, restTimer.workoutId)
                    putExtra(EXTRA_WORKOUT_NAME, restTimer.workoutName)
                    putExtra(EXTRA_EXERCISE_NAME, restTimer.exerciseName)
                }
            }

            return PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}
