package com.daniloscataloni.liftking.resttimer

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.CopyOnWriteArraySet

class AppVisibilityTracker : DefaultLifecycleObserver {

    private val listeners = CopyOnWriteArraySet<(Boolean) -> Unit>()

    var isAppInForeground: Boolean = true
        private set

    override fun onStart(owner: LifecycleOwner) {
        updateAppVisibility(isForeground = true)
    }

    override fun onStop(owner: LifecycleOwner) {
        updateAppVisibility(isForeground = false)
    }

    fun addListener(listener: (Boolean) -> Unit) {
        listeners += listener
    }

    private fun updateAppVisibility(isForeground: Boolean) {
        isAppInForeground = isForeground
        listeners.forEach { listener -> listener(isForeground) }
    }
}
