package com.fabian.androidplayground.common.recyclerview

import android.view.View
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fabian.androidplayground.common.recyclerview.views.BindingViewHolder
import com.fabian.androidplayground.common.recyclerview.views.ItemClickListener
import com.fabian.androidplayground.common.recyclerview.views.LifeCycleBindingViewHolder

abstract class LifeCycleItemClickPagingAdapter<T : Any>(diffCallback: DiffUtil.ItemCallback<T>) : LifecycleDatabindingPagingAdapter<T>(diffCallback) {

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