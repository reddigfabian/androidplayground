package com.fabian.androidplayground.common.recyclerview

import androidx.annotation.CallSuper
import androidx.lifecycle.*
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fabian.androidplayground.common.datastructures.WeakMutableSet
import com.fabian.androidplayground.common.recyclerview.viewmodels.ItemViewModel
import com.fabian.androidplayground.common.recyclerview.views.BindingViewHolder

abstract class LifecycleDatabindingVMProviderPagingAdapter<T : Any>(val viewModelProviderOwner : ViewModelStoreOwner, diffCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, BindingViewHolder>(diffCallback),
    LifecycleObserver {

    private val viewHolders = WeakMutableSet<BindingViewHolder>()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroyViewHolders() {
        viewHolders.forEach {
            it.lifecycleDestroy()
        }
    }

    @CallSuper
    override fun onViewAttachedToWindow(holder: BindingViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attachToWindow()
        viewHolders.add(holder)
    }

    @CallSuper
    override fun onViewDetachedFromWindow(holder: BindingViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.detachFromWindow()
        viewHolders.remove(holder)
    }

    inline fun <reified VM : ItemViewModel<T>> provideViewModelItem(key:String, data : T) : VM{
        val get = ViewModelProvider(viewModelProviderOwner).get(key, VM::class.java)
        get.setData(data)
        return get
    }
}