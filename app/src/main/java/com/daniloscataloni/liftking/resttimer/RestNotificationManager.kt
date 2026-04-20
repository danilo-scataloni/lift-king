package com.daniloscataloni.liftking.resttimer

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.daniloscataloni.liftking.MainActivity
import com.daniloscataloni.liftking.R

class RestNotificationManager(
    private val context: Context
) {

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val alertChannel = NotificationChannel(
            REST_CHANNEL_ID,
            context.getString(R.string.notification_rest_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.notification_rest_channel_description)
            enableVibration(true)
            vibrationPattern = longArrayOf(0, 250, 120, 250)
        }

        val ongoingChannel = NotificationChannel(
            REST_ONGOING_CHANNEL_ID,
            context.getString(R.string.notification_rest_ongoing_channel_name),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = context.getString(R.string.notification_rest_ongoing_channel_description)
            setSound(null, null)
            enableVibration(false)
        }

        context.getSystemService(NotificationManager::class.java).apply {
            createNotificationChannel(alertChannel)
            createNotificationChannel(ongoingChannel)
        }
    }

    fun showOngoingRestTimerNotification(restTimer: com.daniloscataloni.liftking.domain.models.RestTimer) {
        if (!canPostNotifications()) return

        val contentText = if (restTimer.workoutName.isBlank()) {
            restTimer.exerciseName
        } else {
            "${restTimer.exerciseName} · ${restTimer.workoutName}"
        }

        val notification = NotificationCompat.Builder(context, REST_ONGOING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_rest_notification)
            .setContentTitle(context.getString(R.string.notification_rest_ongoing_title))
            .setContentText(contentText)
            .setContentIntent(MainActivity.createOpenTrainingPendingIntent(context, restTimer.workoutId))
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSilent(true)
            .setShowWhen(true)
            .setWhen(restTimer.endAtEpochMillis)
            .setUsesChronometer(true)
            .setChronometerCountDown(true)
            .setTimeoutAfter((restTimer.endAtEpochMillis - System.currentTimeMillis()).coerceAtLeast(0L))
            .build()

        NotificationManagerCompat.from(context).notify(REST_ONGOING_NOTIFICATION_ID, notification)
    }

    fun cancelOngoingRestTimerNotification() {
        NotificationManagerCompat.from(context).cancel(REST_ONGOING_NOTIFICATION_ID)
    }

    fun showRestTimerFinishedNotification(workoutId: Long, workoutName: String, exerciseName: String) {
        if (!canPostNotifications()) return

        val contentIntent = MainActivity.createOpenTrainingPendingIntent(context, workoutId)
        val contentText = if (workoutName.isBlank()) {
            context.getString(R.string.notification_rest_message_no_workout, exerciseName)
        } else {
            context.getString(R.string.notification_rest_message, exerciseName, workoutName)
        }

        val notification = NotificationCompat.Builder(context, REST_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_rest_notification)
            .setContentTitle(context.getString(R.string.notification_rest_title))
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 250, 120, 250))
            .build()

        NotificationManagerCompat.from(context).notify(REST_FINISHED_NOTIFICATION_ID, notification)
    }

    private fun canPostNotifications(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val REST_CHANNEL_ID = "rest_timer_alerts"
        const val REST_ONGOING_CHANNEL_ID = "rest_timer_ongoing"
        private const val REST_ONGOING_NOTIFICATION_ID = 1001
        private const val REST_FINISHED_NOTIFICATION_ID = 1002
    }
}
