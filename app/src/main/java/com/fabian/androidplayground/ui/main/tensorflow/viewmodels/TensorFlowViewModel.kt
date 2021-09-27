package com.fabian.androidplayground.ui.main.tensorflow.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.EditText
import androidx.core.graphics.drawable.toBitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.ImageRequest
import coil.transform.BlurTransformation
import com.fabian.androidplayground.common.coil.BlurIfPornTransformation
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.ui.main.coroutines.viewmodels.CoroutinesViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class TensorFlowViewModel private constructor(dataStore: DataStore<Preferences>): BaseFragmentViewModel(dataStore) {


    override val TAG = "TensorFlowViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            TensorFlowViewModel(dataStore) as T
    }

    val drawable = MutableLiveData<Drawable?>()
    val result = MutableLiveData<String?>()

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