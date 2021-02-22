package com.fabian.androidplayground.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LottieAnimationViewModel : ViewModel() {
    val playState = MutableLiveData(false)
    val speed = MutableLiveData(1)

    fun toggle() {
        playState.value = !(playState.value ?: false)
    }
}