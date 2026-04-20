package com.daniloscataloni.liftking.resttimer

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import com.daniloscataloni.liftking.domain.models.RestTimer

enum class RestTimerScheduleMode {
    EXACT,
    INEXACT
}

data class RestTimerScheduleResult(
    val timer: RestTimer,
    val scheduleMode: RestTimerScheduleMode
)

interface IRestTimerManager {
    fun getActiveTimer(): RestTimer?
    fun getLastUsedDurationSeconds(): Int
    fun scheduleRestTimer(restTimer: RestTimer): RestTimerScheduleResult
    fun cancelActiveTimer()
}

class RestTimerManager(
    private val context: Context,
    private val appVisibilityTracker: AppVisibilityTracker
) : IRestTimerManager {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    private val storage = RestTimerStorage(context)
    private val notificationManager = RestNotificationManager(context)

    init {
        syncOngoingNotification()
        appVisibilityTracker.addListener {
            syncOngoingNotification()
        }
    }

    override fun getActiveTimer(): RestTimer? = storage.getActiveTimer()

    override fun getLastUsedDurationSeconds(): Int = storage.getLastUsedDurationSeconds()

    override fun scheduleRestTimer(restTimer: RestTimer): RestTimerScheduleResult {
        cancelScheduledAlarmOnly()

        storage.saveActiveTimer(restTimer)
        storage.saveLastUsedDurationSeconds(restTimer.durationSeconds)

        val pendingIntent = RestTimerReceiver.createPendingIntent(context, restTimer)
        val scheduleMode = if (canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                restTimer.endAtEpochMillis,
                pendingIntent
            )
            RestTimerScheduleMode.EXACT
        } else {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                restTimer.endAtEpochMillis,
                pendingIntent
            )
            RestTimerScheduleMode.INEXACT
        }

        syncOngoingNotification()

        return RestTimerScheduleResult(restTimer, scheduleMode)
    }

    override fun cancelActiveTimer() {
        cancelScheduledAlarmOnly()
        storage.clearActiveTimer()
        notificationManager.cancelOngoingRestTimerNotification()
    }

    private fun cancelScheduledAlarmOnly() {
        alarmManager.cancel(RestTimerReceiver.createPendingIntent(context))
    }

    private fun canScheduleExactAlarms(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()
    }

    private fun syncOngoingNotification() {
        val activeTimer = getActiveTimer()
        if (activeTimer != null && !appVisibilityTracker.isAppInForeground) {
            notificationManager.showOngoingRestTimerNotification(activeTimer)
        } else {
            notificationManager.cancelOngoingRestTimerNotification()
        }
    }
}
