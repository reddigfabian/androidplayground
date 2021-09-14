package com.fabian.androidplayground.ui.main.finnhub.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.DiffUtil
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.finnhub.FinnhubStockSymbol
import com.fabian.androidplayground.common.recyclerview.LifeCycleItemClickPagingAdapter
import com.fabian.androidplayground.common.recyclerview.views.BindingViewHolder
import com.fabian.androidplayground.common.recyclerview.views.LifeCycleBindingViewHolder
import com.fabian.androidplayground.databinding.ItemFinnhubListBinding
import com.fabian.androidplayground.ui.main.finnhub.list.viewmodels.FinnhubItemViewModel
import com.fabian.androidplayground.ui.main.finnhub.list.viewmodels.toRecyclerItem
import kotlinx.coroutines.FlowPreview
import java.util.*

@FlowPreview
class FinnhubListAdapter(private val lifecycleOwner: LifecycleOwner) : LifeCycleItemClickPagingAdapter<FinnhubItemViewModel>(ITEM_COMPARATOR) {

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun setData(pagingData: PagingData<FinnhubStockSymbol>) {
        submitData(lifecycleOwner.lifecycle, pagingData
            .map { picsum ->
                FinnhubItemViewModel(picsum)
            }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LifeCycleBindingViewHolder {
        return LifeCycleBindingViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_finnhub_list, parent, false))
    }

    override fun onBindViewHolder(holder: LifeCycleBindingViewHolder, position: Int) {
        getItem(position)?.let { item ->
            (holder.binding as? ItemFinnhubListBinding)?.let { itemMainListBinding ->
                item.toRecyclerItem().bind(holder.binding)
                holder.lifecycle.addObserver(item)
                itemMainListBinding.finnhubListItemRoot.setOnClickListener { notifyListenersOfClick(item, it) }
                itemMainListBinding.finnhubListItemRoot.setOnLongClickListener { notifyListenersOfLongClick(item, it) }
                itemMainListBinding.executePendingBindings()
            }
        }
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<FinnhubItemViewModel>() {
            override fun areContentsTheSame(oldItem: FinnhubItemViewModel, newItem: FinnhubItemViewModel): Boolean =
                oldItem.finnhubStock == newItem.finnhubStock

            override fun areItemsTheSame(oldItem: FinnhubItemViewModel, newItem: FinnhubItemViewModel): Boolean =
                oldItem == newItem
        }
    }
}