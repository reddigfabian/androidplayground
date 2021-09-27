package com.fabian.androidplayground.common.databinding

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ListenerUtil
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.glide.ThumbnailCrossFadeFactory
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

const val THUMBNAIL_SIZE_PERCENT = 0.1

@BindingAdapter(value = ["picsum", "picsumSize"], requireAll = false)
fun picsum(view : DynamicHeightImageView, picsum : Picsum?, sizePercent : Double?) {
    val sizePercentInternal = if (sizePercent == null || sizePercent == 0.0) {
        THUMBNAIL_SIZE_PERCENT
    } else {
        sizePercent
    }

    picsum?.let { pic ->
        val isThumbnail = sizePercentInternal == THUMBNAIL_SIZE_PERCENT
        view.heightRatio = pic.heightRatio
        val uri = Uri.parse(pic.download_url)
        val uriPathSegments = uri.pathSegments
        val thumbnailPathSegments = uriPathSegments.toMutableList()
        thumbnailPathSegments[thumbnailPathSegments.lastIndex-1] = (Integer.valueOf(thumbnailPathSegments[thumbnailPathSegments.lastIndex - 1]) * THUMBNAIL_SIZE_PERCENT).toInt().toString()
        thumbnailPathSegments[thumbnailPathSegments.lastIndex] = (Integer.valueOf(thumbnailPathSegments[thumbnailPathSegments.lastIndex]) * THUMBNAIL_SIZE_PERCENT).toInt().toString()
        val thumbnailBuilder = Uri.Builder().scheme(uri.scheme).authority(uri.authority)
        thumbnailPathSegments.forEach {
            thumbnailBuilder.appendPath(it)
        }
        val thumbnailUri = thumbnailBuilder.build()
        if (isThumbnail) {
            Glide.with(view.context)
                .load(thumbnailUri)
                .placeholder(ColorDrawable(Color.DKGRAY))
                .error(ColorDrawable(Color.RED))
                .into(view)
        } else {
            val newPathSegments = uriPathSegments.toMutableList()
            newPathSegments[newPathSegments.lastIndex-1] = (Integer.valueOf(newPathSegments[newPathSegments.lastIndex - 1]) * sizePercentInternal).toInt().toString()
            newPathSegments[newPathSegments.lastIndex] = (Integer.valueOf(newPathSegments[newPathSegments.lastIndex]) * sizePercentInternal).toInt().toString()
            val builder = Uri.Builder().scheme(uri.scheme).authority(uri.authority)
            newPathSegments.forEach {
                builder.appendPath(it)
            }
            val newUri = builder.build()

            Glide.with(view.context)
                .load(newUri)
                .thumbnail(Glide
                    .with(view.context)
                    .load(thumbnailUri))
                .error(ColorDrawable(Color.RED))
                .transition(withCrossFade(ThumbnailCrossFadeFactory.Builder()))
                .into(view)
        }

        //Coil
//        view.load(newUri) {
//            placeholder(ColorDrawable(Color.DKGRAY))
//            error(ColorDrawable(Color.RED))
//        }
    }
}

@BindingAdapter("imageUri")
fun imageUri(imageView : ImageView, uri : Uri) {
    imageView.setImageURI(uri)
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