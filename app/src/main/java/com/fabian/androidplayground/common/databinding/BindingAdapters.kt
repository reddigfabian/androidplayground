package com.fabian.androidplayground.common.databinding

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import androidx.databinding.BindingAdapter
import coil.load
import com.airbnb.lottie.LottieAnimationView
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

@BindingAdapter(value = ["picsum", "picsumSize"], requireAll = true)
fun picsum(view : DynamicHeightImageView, pic : Picsum, sizePercent : Double = 0.05) {
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
        crossfade(true)
        placeholder(ColorDrawable(Color.DKGRAY))
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

//@BindingAdapter("onItemClick")
//fun <T : Any> onItemClick(recycler : RecyclerView, clickFunction : (T) -> Unit) {
//    val adapters = mutableListOf(recycler.adapter)
//    if (recycler.adapter is ConcatAdapter) {
//        adapters.clear()
//        (recycler.adapter as? ConcatAdapter)?.adapters?.forEach { concatAdapter ->
//            (concatAdapter as? CustomPagingAdapter<T>)?.let { customAdapter ->
//                adapters.add(customAdapter)
//            }
//        }
//    }
//
//    adapters.forEach {
//        (it as? CustomPagingAdapter<T>)?.listener = object : CustomPagingAdapter.PagingItemClickListener<T> {
//            override fun onItemClick(item: T) {
//                clickFunction.invoke(item)
//            }
//        }
//    }
//}