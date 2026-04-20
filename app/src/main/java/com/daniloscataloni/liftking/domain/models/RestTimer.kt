package com.daniloscataloni.liftking.domain.models

data class RestTimer(
    val workoutId: Long,
    val exerciseId: Int,
    val exerciseName: String,
    val workoutName: String,
    val durationSeconds: Int,
    val startAtEpochMillis: Long,
    val endAtEpochMillis: Long
)
