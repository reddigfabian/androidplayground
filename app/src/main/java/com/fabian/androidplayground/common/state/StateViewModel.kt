package com.fabian.androidplayground.common.state

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
@FlowPreview
abstract class StateViewModel<T : State> : ViewModel() {
    inline fun <reified T : State> setState(newState: T) {
        StateManagerFactory.getStateManager(T::class.java).setState(newState)
    }

    inline fun <reified T : State> getState(): T {
        return StateManagerFactory.getStateManager(T::class.java).getState()
    }

    inline fun <reified T : State> getStateAsFlow(): Flow<T> {
        return StateManagerFactory.getStateManager(T::class.java).getStateAsFlow()
    }

    open fun onBackPressed() {}
}