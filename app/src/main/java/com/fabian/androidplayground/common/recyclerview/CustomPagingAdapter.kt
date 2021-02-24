package com.fabian.androidplayground.common.recyclerview

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fabian.androidplayground.common.databinding.BindingViewHolder
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class ItemClickPagingAdapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) : PagingDataAdapter<T, BindingViewHolder>(diffCallback) {

    interface PagingItemClickListener<T>{
        fun onItemClick(item : T)
    }

    val itemClickFlow = MutableSharedFlow<T>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

//    var listener : PagingItemClickListener<T>? = null

    protected fun onItemClick(item: T) {
        itemClickFlow.tryEmit(item)
//        listener?.onItemClick(item)
    }
}