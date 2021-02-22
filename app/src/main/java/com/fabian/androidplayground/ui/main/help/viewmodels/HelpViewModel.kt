package com.fabian.androidplayground.ui.main.help.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel

class HelpViewModel : ViewModel() {
    val nextEventChannel = Channel<Unit>(Channel.CONFLATED)

    fun onConfirmClick(v : View) {
        nextEventChannel.offer(Unit)
    }
}