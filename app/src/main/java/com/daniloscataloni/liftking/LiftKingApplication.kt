package com.daniloscataloni.liftking

import android.app.Application
import com.daniloscataloni.liftking.di.appModule
import com.daniloscataloni.liftking.resttimer.AppVisibilityTracker
import com.daniloscataloni.liftking.resttimer.RestNotificationManager
import androidx.lifecycle.ProcessLifecycleOwner
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.ext.android.getKoin
import org.koin.core.context.startKoin

class LiftKingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LiftKingApplication)
            androidLogger()
            modules(appModule)
        }
        RestNotificationManager(this).createNotificationChannels()
        ProcessLifecycleOwner.get().lifecycle.addObserver(getKoin().get<AppVisibilityTracker>())
    }
}
