package com.fabian.androidplayground.ui.main.audio.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel

class AudioViewModel : ViewModel() {
    val nextEventChannel = Channel<Unit>(Channel.CONFLATED)

    fun onConfirmClick(v : View) {
        nextEventChannel.offer(Unit)
    }
}