package com.fabian.androidplayground.common.state

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@FlowPreview
@ExperimentalCoroutinesApi
object StateManagerFactory {
    private val stateManagers : ConcurrentMap<String, StateManagerImpl<State>> = ConcurrentHashMap()

    fun <T : State> getStateManager(state : Class<T>) : StateManagerImpl<T> {
        return stateManagers.computeIfAbsent(state.name) {
            return@computeIfAbsent StateManagerImpl(state.name, state.newInstance())
        } as StateManagerImpl<T>
    }

    fun removeStateManager(key : String) {
        stateManagers.remove(key)
    }
}