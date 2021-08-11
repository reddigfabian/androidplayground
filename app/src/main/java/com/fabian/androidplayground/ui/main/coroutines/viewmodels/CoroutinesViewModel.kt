package com.fabian.androidplayground.ui.main.coroutines.viewmodels

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import java.util.concurrent.locks.ReentrantLock
import kotlin.random.Random

private const val TAG = "CoroutinesViewModel"

class CoroutinesViewModel : ViewModel() {

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