package com.falon.trendingprojects.util

import android.view.View
import android.view.animation.AccelerateInterpolator

internal fun View.shrinkAndFadeAnimationRun(
    scaleEnd: Float = 0.35f,
    alphaEnd: Float = 0f,
    durationMillis: Long = 500L,
    endAction: () -> Unit,
) =
    animate()?.apply {
        scaleX(scaleEnd)
        scaleY(scaleEnd)
        alpha(alphaEnd)
        interpolator = AccelerateInterpolator()
        duration = durationMillis
        withEndAction { endAction() }
        start()
    }
