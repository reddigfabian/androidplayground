package com.fabian.androidplayground.common.state

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
@FlowPreview
class StateManagerImpl<T : State>(key : String, defaultState : T) : StateManagerInterface<T> {
    private val mutableStateFlow = MutableStateFlow(defaultState)
    private val stateFlow = mutableStateFlow.map {
        if (it.viewState is ExitState) {
            StateManagerFactory.removeStateManager(key)
        }
        it
    }

    override fun setState(newState : T) {
        mutableStateFlow.value = newState
    }

    override fun getState(): T {
        return mutableStateFlow.value
    }

    override fun getStateAsFlow(): Flow<T> {
        return stateFlow
    }
}