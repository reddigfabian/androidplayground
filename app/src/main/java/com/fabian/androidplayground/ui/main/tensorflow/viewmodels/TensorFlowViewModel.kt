package com.fabian.androidplayground.ui.main.tensorflow.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.EditText
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.ImageRequest
import coil.transform.BlurTransformation
import com.fabian.androidplayground.common.coil.BlurIfPornTransformation
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

private const val TAG = "TensorFlowViewModel"

class TensorFlowViewModel : ViewModel() {

    val drawable = MutableLiveData<Drawable?>()
    val result = MutableLiveData<String?>()
    private val options = ImageClassifier.ImageClassifierOptions.builder().setMaxResults(1).build()
    private var imageClassifier : ImageClassifier? = null

    val textChangeResponse : (EditText) -> Unit = { editText ->
        result.postValue("")
        imageLoad(editText.context, editText.text.toString())
    }

    private var stuff = ""

    private fun imageLoad(context: Context, url : String?) {
        url?.let { urlString ->
            stuff = urlString
            val imageLoader = context.imageLoader
            val requestBuilder = ImageRequest.Builder(context)
                .data(urlString)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .target(onSuccess = {
                    drawable.value = it
                }, onError = {
                    drawable.value = null
                })
                .transformations(BlurIfPornTransformation(context, 25f, 100f))
            imageLoader.enqueue(requestBuilder.build())
        }?: run {
            drawable.postValue(null)
        }
    }
}