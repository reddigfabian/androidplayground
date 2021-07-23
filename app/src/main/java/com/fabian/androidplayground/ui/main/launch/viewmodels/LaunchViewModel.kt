package com.fabian.androidplayground.ui.main.launch.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.fabian.androidplayground.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class LaunchViewModel : ViewModel() {
    fun onClick(view : View) {
        when (view.id) {
            R.id.picsumButton -> {
//                setState(MainState(MainState.Picsum))
            }
            R.id.finnhubButton -> {
//                setState(MainState(MainState.Finnhub))
            }
        }
    }
}