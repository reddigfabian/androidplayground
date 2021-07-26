package com.fabian.androidplayground.common.recyclerview

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fabian.androidplayground.common.recyclerview.views.BindingViewHolder
import com.fabian.androidplayground.common.recyclerview.views.RecyclerStateItemViewHolder

class LoadStateAdapter<T : Any>(private val adapter: PagingDataAdapter<T, BindingViewHolder>) : LoadStateAdapter<RecyclerStateItemViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerStateItemViewHolder, loadState: LoadState) {
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            layoutParams.isFullSpan = true
        }
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RecyclerStateItemViewHolder {
        return RecyclerStateItemViewHolder(parent) { adapter.retry() }
    }
}