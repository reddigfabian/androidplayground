package com.fabian.androidplayground.common.state

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@FlowPreview
@ExperimentalCoroutinesApi
interface StateManagerInterface<T : State> {
    fun setState(newState : T)
    fun getState() : T
    fun getStateAsFlow(): Flow<T>
}