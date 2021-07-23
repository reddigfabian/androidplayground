package com.fabian.androidplayground.ui.main.finnhub.detail.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.fabian.androidplayground.api.picsum.Picsum
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class FinnhubDetailViewModel : ViewModel() {
    var pic: Picsum? = null
}