package com.fabian.androidplayground.common.recyclerview

import android.view.View
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fabian.androidplayground.common.recyclerview.views.BindingViewHolder

abstract class ItemClickPagingAdapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) : PagingDataAdapter<T, BindingViewHolder>(diffCallback) {

    interface ItemClickListener<T> {
        fun onItemClick(item : T, view : View)
        fun onItemLongClick(item : T, view : View)
    }

    private val listeners = mutableSetOf<ItemClickListener<T>>()

    fun addItemClickListener(listener : ItemClickListener<T>) {
        listeners.add(listener)
    }

    fun removeItemClickListener(listener : ItemClickListener<T>) {
        listeners.remove(listener)
    }

    fun notifyListenersOfClick(item : T, view : View) {
        listeners.forEach { listener ->
            listener.onItemClick(item, view)
        }
    }

    fun notifyListenersOfLongClick(item : T, view : View) : Boolean {
        var handled = false
        listeners.forEach { listener ->
            listener.onItemLongClick(item, view)
            handled = true
        }
        return handled
    }
}