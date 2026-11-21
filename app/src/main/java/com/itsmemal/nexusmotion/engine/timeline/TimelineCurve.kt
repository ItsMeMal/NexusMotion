package com.itsmemal.nexusmotion.engine.timeline

enum class KeyframeInterpolation {
    HOLD,
    LINEAR,
    EASE_IN_OUT
}

data class ScalarKeyframe(
    val frame: Int,
    val value: Float,
    val interpolationToNext: KeyframeInterpolation = KeyframeInterpolation.LINEAR
)

/**
 * Stateless scalar track evaluator. A keyframe's interpolation mode controls the
 * segment from that keyframe to the next one.
 */
object TimelineCurve {
    fun sample(keyframes: List<ScalarKeyframe>, frame: Float): Float? {
        if (keyframes.isEmpty()) return null

        val ordered = keyframes.sortedBy(ScalarKeyframe::frame)
        val first = ordered.first()
        val last = ordered.last()

        if (frame <= first.frame) return first.value
        if (frame >= last.frame) return last.value

        val rightIndex = ordered.indexOfFirst { it.frame >= frame }
        if (rightIndex <= 0) return first.value

        val left = ordered[rightIndex - 1]
        val right = ordered[rightIndex]
        val span = (right.frame - left.frame).toFloat()
        if (span <= 0f) return right.value

        val rawProgress = ((frame - left.frame) / span).coerceIn(0f, 1f)
        val progress = when (left.interpolationToNext) {
            KeyframeInterpolation.HOLD -> 0f
            KeyframeInterpolation.LINEAR -> rawProgress
            KeyframeInterpolation.EASE_IN_OUT -> rawProgress * rawProgress * (3f - 2f * rawProgress)
        }

        return left.value + (right.value - left.value) * progress
    }
}
