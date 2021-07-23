package com.fabian.androidplayground.ui.main.picsum.viewmodels

import com.fabian.androidplayground.common.state.ExitState
import com.fabian.androidplayground.common.state.StateViewModel
import com.fabian.androidplayground.ui.main.picsum.state.LoremPicsumState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class LoremPicsumViewModel : StateViewModel<LoremPicsumState>() {
    override fun onBackPressed() {
        when (getState<LoremPicsumState>().viewState) {
            LoremPicsumState.Detail -> {
                setState(LoremPicsumState(LoremPicsumState.List))
            }
            LoremPicsumState.List -> {
                setState(LoremPicsumState(ExitState))
            }
        }
    }
}