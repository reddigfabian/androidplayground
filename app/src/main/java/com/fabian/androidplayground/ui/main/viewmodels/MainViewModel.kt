package com.fabian.androidplayground.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class MainViewModel : ViewModel() {

    private val titleResID = MutableLiveData<Int>()

    fun getTitleResID() : LiveData<Int> {
        return titleResID
    }
}