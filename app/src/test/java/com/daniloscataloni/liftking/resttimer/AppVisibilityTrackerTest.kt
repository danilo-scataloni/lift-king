package com.daniloscataloni.liftking.resttimer

import android.app.Activity
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AppVisibilityTrackerTest {

    @Test
    fun `marks app as background only after the last activity stops`() {
        val tracker = AppVisibilityTracker()
        val firstActivity = mockk<Activity>()
        val secondActivity = mockk<Activity>()

        every { firstActivity.isChangingConfigurations } returns false
        every { secondActivity.isChangingConfigurations } returns false

        tracker.onActivityStarted(firstActivity)
        tracker.onActivityStarted(secondActivity)
        tracker.onActivityStopped(firstActivity)

        assertTrue(tracker.isAppInForeground)

        tracker.onActivityStopped(secondActivity)

        assertFalse(tracker.isAppInForeground)
    }

    @Test
    fun `keeps app in foreground during configuration change`() {
        val tracker = AppVisibilityTracker()
        val recreatedActivity = mockk<Activity>()
        val newActivity = mockk<Activity>()

        every { recreatedActivity.isChangingConfigurations } returns true
        every { newActivity.isChangingConfigurations } returns false

        tracker.onActivityStarted(recreatedActivity)
        tracker.onActivityStopped(recreatedActivity)

        assertTrue(tracker.isAppInForeground)

        tracker.onActivityStarted(newActivity)
        tracker.onActivityStopped(newActivity)

        assertFalse(tracker.isAppInForeground)
    }
}
