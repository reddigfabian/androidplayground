package com.fabian.androidplayground.common.animation

import android.animation.TimeInterpolator
import android.util.Log
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp

private const val TAG = "SwingInterpolator"

class SwingInterpolator : TimeInterpolator {

    override fun getInterpolation(p: Float): Float {
        var t = p
        t = (exp(-6*t) * cos((20.5f * PI.toFloat() * t) + 3)) + 1.0f
        return t
    }
}