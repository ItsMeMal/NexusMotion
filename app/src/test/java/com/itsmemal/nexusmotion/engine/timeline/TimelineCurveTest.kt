package com.itsmemal.nexusmotion.engine.timeline

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TimelineCurveTest {
    @Test
    fun emptyTrackReturnsNull() {
        assertNull(TimelineCurve.sample(emptyList(), 12f))
    }

    @Test
    fun linearTrackInterpolatesBetweenFrames() {
        val track = listOf(
            ScalarKeyframe(0, 0f),
            ScalarKeyframe(20, 100f)
        )

        assertEquals(25f, TimelineCurve.sample(track, 5f)!!, 0.0001f)
        assertEquals(50f, TimelineCurve.sample(track, 10f)!!, 0.0001f)
    }

    @Test
    fun holdTrackKeepsLeftValueUntilNextKeyframe() {
        val track = listOf(
            ScalarKeyframe(0, 12f, KeyframeInterpolation.HOLD),
            ScalarKeyframe(10, 80f)
        )

        assertEquals(12f, TimelineCurve.sample(track, 9.5f)!!, 0.0001f)
        assertEquals(80f, TimelineCurve.sample(track, 10f)!!, 0.0001f)
    }

    @Test
    fun valuesOutsideTrackUseNearestKeyframe() {
        val track = listOf(
            ScalarKeyframe(8, 4f),
            ScalarKeyframe(16, 20f)
        )

        assertEquals(4f, TimelineCurve.sample(track, 0f)!!, 0.0001f)
        assertEquals(20f, TimelineCurve.sample(track, 30f)!!, 0.0001f)
    }
}
