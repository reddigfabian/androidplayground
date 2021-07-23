package com.fabian.androidplayground.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabian.androidplayground.common.state.StateManagerFactory
import com.fabian.androidplayground.common.state.StateManagerInterface
import com.fabian.androidplayground.ui.main.state.MainState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class MainViewModel : ViewModel(), StateManagerInterface<MainState> by StateManagerFactory.getStateManager(MainState::class.java) {

    private val titleResID = MutableLiveData<Int>()

    fun getTitleResID() : LiveData<Int> {
        return titleResID
    }
}