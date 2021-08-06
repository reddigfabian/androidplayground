package com.fabian.androidplayground.common.databinding

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ListenerUtil
import coil.load
import com.airbnb.lottie.LottieAnimationView
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.views.DynamicHeightImageView

@BindingAdapter("lottie_play")
fun playState(animationView : LottieAnimationView, play : Boolean) {
    if (play) {
        animationView.resumeAnimation()
    } else {
        animationView.pauseAnimation()
    }
}

@BindingAdapter("lottie_speed")
fun playSpeed(animationView : LottieAnimationView, speed : Float) {
    animationView.speed = speed.toFloat()
}

const val THUMBNAIL_SIZE = 0.01

@BindingAdapter(value = ["picsum", "picsumSize"], requireAll = false)
fun picsum(view : DynamicHeightImageView, picsum : Picsum?, sizePercent : Double = THUMBNAIL_SIZE) {
    picsum?.let { pic ->
        view.heightRatio = pic.heightRatio
        val uri = Uri.parse(pic.download_url)
        val newPathSegments = uri.pathSegments.toMutableList()
        newPathSegments[newPathSegments.lastIndex-1] = (Integer.valueOf(newPathSegments[newPathSegments.lastIndex - 1]) * sizePercent).toInt().toString()
        newPathSegments[newPathSegments.lastIndex] = (Integer.valueOf(newPathSegments[newPathSegments.lastIndex]) * sizePercent).toInt().toString()

        val builder = Uri.Builder().scheme(uri.scheme).authority(uri.authority)
        newPathSegments.forEach {
            builder.appendPath(it)
        }
        val newUri = builder.build()

        view.load(newUri) {
            placeholder(ColorDrawable(Color.DKGRAY))
        }
    }
}

@BindingAdapter("isVisibleGone")
fun isVisibleGone(view : View, visible : Boolean) {
    if (visible) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("textChangeFunction")
fun textChangeFunction(editText: EditText, function : (EditText) -> Unit) {
    val newValue = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            function.invoke(editText)
        }
    }

    val oldValue = ListenerUtil.trackListener<TextWatcher>(editText, newValue, R.id.textWatcher)
    if (oldValue != null) {
        editText.removeTextChangedListener(oldValue)
    }
    editText.addTextChangedListener(newValue)
}