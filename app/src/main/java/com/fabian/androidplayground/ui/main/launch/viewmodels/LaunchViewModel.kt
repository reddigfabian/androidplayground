package com.fabian.androidplayground.ui.main.launch.viewmodels

import android.view.View
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.state.ExitState
import com.fabian.androidplayground.common.state.StateViewModel
import com.fabian.androidplayground.ui.main.state.MainState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class LaunchViewModel : StateViewModel<MainState>() {

    fun onClick(view : View) {
        when (view.id) {
            R.id.picsumButton -> {
                setState(MainState(MainState.Picsum))
            }
            R.id.finnhubButton -> {
                setState(MainState(MainState.Finnhub))
            }
        }
    }

    override fun onBackPressed() {
        when (getState<MainState>().viewState) {
            MainState.Picsum,
            MainState.Finnhub -> {
                setState(MainState(MainState.Launch))
            }
            MainState.Launch -> {
                setState(MainState(ExitState))
            }
        }
    }
}