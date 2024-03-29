package com.fabian.androidplayground.common.scarlet

import com.tinder.scarlet.Stream
import com.tinder.scarlet.StreamAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow

class FlowStreamAdapter<T : Any> : StreamAdapter<T, Flow<T>> {
    override fun adapt(stream: Stream<T>) : Flow<T> {
        return stream.asFlow()
    }
}