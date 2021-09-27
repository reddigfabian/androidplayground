package com.fabian.androidplayground.ui.main.coroutines.viewmodels

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class CoroutinesViewModel private constructor(dataStore : DataStore<Preferences>) : BaseFragmentViewModel(dataStore) {

    override val TAG = "CoroutinesViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            CoroutinesViewModel(dataStore) as T
    }

    private val threadNameToColorMap = mutableMapOf(
        "main" to Color.BLACK
    )

    private val rand = Random(hashCode())

    private var statusText = SpannableStringBuilder()
    private var threadMapText = SpannableStringBuilder()
    val statusTextLiveData = MutableLiveData<SpannableStringBuilder>()
    val threadMapTextLiveData = MutableLiveData<SpannableStringBuilder>()

    init {
        helloWorld()
        repeat(10) {
            viewModelScope.launch {
                helloWorldSuspend()
            }
        }
    }

    private fun helloWorld() {
        printLine("Hello World!")
    }

    private suspend fun helloWorldSuspend() = withContext(Default) { // this: CoroutineScope
        viewModelScope.launch { // launch a new coroutine and continue
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            printLine("World!") // print after delay
        }
        print("Hello ") // main coroutine continues while a previous one is delayed
    }

    private fun printLine(s : String) {
        print("$s\n")
    }

    private fun print(s : String) {
        statusText.append(s, ForegroundColorSpan(threadNameToColorMap.getOrPut(Thread.currentThread().name) { Color.rgb(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()) }), 0)
        statusTextLiveData.postValue(statusText)
    }

    private fun printThreadMap() {
        threadMapText.clear()
        threadMapText.clearSpans()
        threadNameToColorMap.forEach {
            threadMapText.append("\u25A0 ${it.key}", ForegroundColorSpan(it.value), 0)
            threadMapText.append("\n")
        }
        threadMapTextLiveData.postValue(threadMapText)
    }
}