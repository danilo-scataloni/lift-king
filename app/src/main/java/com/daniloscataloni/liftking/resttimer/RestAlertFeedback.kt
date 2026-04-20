package com.daniloscataloni.liftking.resttimer

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
fun vibrateForRest(context: Context) {
    val vibrationEffect = VibrationEffect.createWaveform(longArrayOf(0, 200, 100, 200), -1)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibrator = context.getSystemService(VibratorManager::class.java).defaultVibrator
        vibrator.vibrate(vibrationEffect)
    } else {
        @Suppress("DEPRECATION")
        val vibrator = context.getSystemService(Vibrator::class.java)
        vibrator?.vibrate(vibrationEffect)
    }
}
