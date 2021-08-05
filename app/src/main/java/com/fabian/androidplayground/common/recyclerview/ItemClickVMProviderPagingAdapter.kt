package com.fabian.androidplayground.common.recyclerview

import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DiffUtil

abstract class ItemClickVMProviderPagingAdapter<T : Any>(viewModelProviderOwner: ViewModelStoreOwner, diffCallback: DiffUtil.ItemCallback<T>) : LifecycleDatabindingVMProviderPagingAdapter<T>(viewModelProviderOwner, diffCallback) {

    interface ItemClickListener<T> {
        fun onItemClick(item : T)
        fun onItemLongClick(item : T)
    }

    private val listeners = mutableSetOf<ItemClickListener<T>>()

    fun addItemClickListener(listener : ItemClickListener<T>) {
        listeners.add(listener)
    }

    fun removeItemClickListener(listener : ItemClickListener<T>) {
        listeners.remove(listener)
    }

    fun notifyListenersOfClick(item : T) {
        listeners.forEach { listener ->
            listener.onItemClick(item)
        }
    }

    fun notifyListenersOfLongClick(item : T) : Boolean {
        var handled = false
        listeners.forEach { listener ->
            listener.onItemLongClick(item)
            handled = true
        }
        return handled
    }
}