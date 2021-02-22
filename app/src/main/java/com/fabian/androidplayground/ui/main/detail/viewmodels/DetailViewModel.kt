package com.fabian.androidplayground.ui.main.detail.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.fabian.androidplayground.api.picsum.Picsum
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class DetailViewModel : ViewModel() {

    var pic: Picsum? = null

    private val clickEventChannel = Channel<Int>(Channel.CONFLATED)

    fun getClickEventFlow() : Flow<Int> {
        return clickEventChannel.receiveAsFlow()
    }

    fun onClick(v : View) {
        clickEventChannel.offer(v.id)
    }
}