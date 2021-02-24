package com.fabian.androidplayground.ui.main.launch.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class LaunchViewModel : ViewModel() {

    private val clickEventChannel = Channel<Int>(Channel.CONFLATED)

    fun getClickEventFlow() : Flow<Int> {
        return clickEventChannel.receiveAsFlow()
    }

    fun onClick(v : View) {
        clickEventChannel.offer(v.id)
    }
}