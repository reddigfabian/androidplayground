package com.fabian.androidplayground.common.recyclerview.viewmodels

import androidx.lifecycle.ViewModel

abstract class ItemViewModel<T : Any> : ViewModel() {
    abstract fun setData(data : T)
}