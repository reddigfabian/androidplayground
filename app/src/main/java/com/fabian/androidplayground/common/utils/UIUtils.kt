package com.fabian.androidplayground.common.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import com.fabian.androidplayground.R
import java.lang.Exception

object UIUtils {
    fun dpToPx(context : Context, dp : Float) : Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
    }

    fun getDrawableForUri(context : Context, uri : Uri) : Drawable? {
        val createSource = ImageDecoder.createSource(context.contentResolver, uri)
        return try {
            ImageDecoder.decodeDrawable(createSource)
        } catch (ex : Exception) {
            AppCompatResources.getDrawable(context, R.drawable.ic_launcher_foreground)
        }
    }
}