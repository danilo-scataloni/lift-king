package com.daniloscataloni.liftking.resttimer

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.concurrent.CopyOnWriteArraySet

class AppVisibilityTracker : Application.ActivityLifecycleCallbacks {

    private val listeners = CopyOnWriteArraySet<(Boolean) -> Unit>()
    private var startedActivityCount = 0

    var isAppInForeground: Boolean = false
        private set

    override fun onActivityStarted(activity: Activity) {
        startedActivityCount += 1
        if (startedActivityCount == 1) {
            updateAppVisibility(isForeground = true)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        startedActivityCount = (startedActivityCount - 1).coerceAtLeast(0)
        if (startedActivityCount == 0 && !activity.isChangingConfigurations) {
            updateAppVisibility(isForeground = false)
        }
    }

    fun addListener(listener: (Boolean) -> Unit) {
        listeners += listener
    }

    private fun updateAppVisibility(isForeground: Boolean) {
        if (this.isAppInForeground == isForeground) return

        this.isAppInForeground = isForeground
        isAppInForegroundForProcess = isForeground
        listeners.forEach { listener -> listener(isForeground) }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

    override fun onActivityResumed(activity: Activity) = Unit

    override fun onActivityPaused(activity: Activity) = Unit

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    override fun onActivityDestroyed(activity: Activity) = Unit

    companion object {
        @Volatile
        private var isAppInForegroundForProcess: Boolean = false

        fun isAppInForegroundForProcess(): Boolean = isAppInForegroundForProcess
    }
}
