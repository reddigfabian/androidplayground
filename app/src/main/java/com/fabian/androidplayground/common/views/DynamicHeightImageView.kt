package com.fabian.androidplayground.common.views
import android.content.Context
import android.util.AttributeSet

class DynamicHeightImageView @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, defStyleAttr : Int = 0, defStyleRes : Int = 0) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {
    var heightRatio = 0.0
        set(value) {
            field = value
            requestLayout()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (heightRatio > 0.0) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = (width * heightRatio).toInt()
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}