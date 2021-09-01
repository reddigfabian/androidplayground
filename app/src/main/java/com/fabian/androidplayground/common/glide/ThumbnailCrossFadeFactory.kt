package com.fabian.androidplayground.common.glide

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.request.transition.NoTransition
import com.bumptech.glide.request.transition.Transition

class ThumbnailCrossFadeFactory private constructor(duration : Int, crossFadeEnabled : Boolean) : DrawableCrossFadeFactory(duration, crossFadeEnabled) {
    override fun build(dataSource: DataSource?, isFirstResource: Boolean) : Transition<Drawable> {
        return if (isFirstResource) NoTransition.get() else super.build(dataSource, isFirstResource)
    }

    class Builder(duration: Int) : DrawableCrossFadeFactory.Builder(duration) {

        companion object {
            private const val DEFAULT_DURATION_MS = 300
        }

        constructor() : this(DEFAULT_DURATION_MS)

        private val durationMillis = duration
        private var isCrossFadeEnabled = false

        override fun setCrossFadeEnabled(isCrossFadeEnabled: Boolean): Builder {
            this.isCrossFadeEnabled = isCrossFadeEnabled
            return this
        }

        override fun build(): ThumbnailCrossFadeFactory {
            return ThumbnailCrossFadeFactory(durationMillis, isCrossFadeEnabled)
        }
    }
}