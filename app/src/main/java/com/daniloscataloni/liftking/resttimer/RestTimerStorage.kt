package com.daniloscataloni.liftking.resttimer

import android.content.Context
import com.daniloscataloni.liftking.domain.models.RestTimer
import androidx.core.content.edit

class RestTimerStorage(context: Context) {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveActiveTimer(restTimer: RestTimer) {
        preferences.edit {
            putLong(KEY_WORKOUT_ID, restTimer.workoutId)
                .putInt(KEY_EXERCISE_ID, restTimer.exerciseId)
                .putString(KEY_EXERCISE_NAME, restTimer.exerciseName)
                .putString(KEY_WORKOUT_NAME, restTimer.workoutName)
                .putInt(KEY_DURATION_SECONDS, restTimer.durationSeconds)
                .putLong(KEY_START_AT, restTimer.startAtEpochMillis)
                .putLong(KEY_END_AT, restTimer.endAtEpochMillis)
        }
    }

    fun getActiveTimer(): RestTimer? {
        if (!preferences.contains(KEY_END_AT)) return null

        val endAtEpochMillis = preferences.getLong(KEY_END_AT, 0L)
        if (endAtEpochMillis <= System.currentTimeMillis()) {
            clearActiveTimer()
            return null
        }

        return RestTimer(
            workoutId = preferences.getLong(KEY_WORKOUT_ID, 0L),
            exerciseId = preferences.getInt(KEY_EXERCISE_ID, 0),
            exerciseName = preferences.getString(KEY_EXERCISE_NAME, "") ?: "",
            workoutName = preferences.getString(KEY_WORKOUT_NAME, "") ?: "",
            durationSeconds = preferences.getInt(KEY_DURATION_SECONDS, DEFAULT_REST_DURATION_SECONDS),
            startAtEpochMillis = preferences.getLong(KEY_START_AT, 0L),
            endAtEpochMillis = endAtEpochMillis
        )
    }

    fun clearActiveTimer() {
        preferences.edit {
            remove(KEY_WORKOUT_ID)
                .remove(KEY_EXERCISE_ID)
                .remove(KEY_EXERCISE_NAME)
                .remove(KEY_WORKOUT_NAME)
                .remove(KEY_DURATION_SECONDS)
                .remove(KEY_START_AT)
                .remove(KEY_END_AT)
        }
    }

    fun saveLastUsedDurationSeconds(durationSeconds: Int) {
        preferences.edit {
            putInt(KEY_LAST_USED_DURATION_SECONDS, durationSeconds)
        }
    }

    fun getLastUsedDurationSeconds(): Int {
        return preferences.getInt(KEY_LAST_USED_DURATION_SECONDS, DEFAULT_REST_DURATION_SECONDS)
    }

    companion object {
        const val DEFAULT_REST_DURATION_SECONDS = 90

        private const val PREFERENCES_NAME = "lift_king_rest_timer"
        private const val KEY_WORKOUT_ID = "workout_id"
        private const val KEY_EXERCISE_ID = "exercise_id"
        private const val KEY_EXERCISE_NAME = "exercise_name"
        private const val KEY_WORKOUT_NAME = "workout_name"
        private const val KEY_DURATION_SECONDS = "duration_seconds"
        private const val KEY_START_AT = "start_at"
        private const val KEY_END_AT = "end_at"
        private const val KEY_LAST_USED_DURATION_SECONDS = "last_used_duration_seconds"
    }
}
