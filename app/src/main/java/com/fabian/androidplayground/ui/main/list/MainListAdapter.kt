package com.fabian.androidplayground.ui.main.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.DiffUtil
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.views.BindingViewHolder
import com.fabian.androidplayground.common.recyclerview.ItemClickPagingAdapter
import com.fabian.androidplayground.databinding.ItemMainListBinding
import com.fabian.androidplayground.ui.main.list.viewmodels.MainListItemViewModel
import com.fabian.androidplayground.ui.main.list.viewmodels.toRecyclerItem
import kotlinx.coroutines.FlowPreview

@FlowPreview
class MainListAdapter(private val lifecycleOwner: LifecycleOwner) : ItemClickPagingAdapter<MainListItemViewModel>(ITEM_COMPARATOR) {

    fun setData(pagingData: PagingData<Picsum>) {
        submitData(lifecycleOwner.lifecycle, pagingData
            .map { picsum ->
                MainListItemViewModel(picsum)
            }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_main_list, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position)?.let { item ->
            (holder.binding as ItemMainListBinding).mainListItemRoot.transitionName = "transition$position"
            item.toRecyclerItem().bind(holder.binding)
            holder.binding.mainListItemRoot.setOnClickListener { notifyListenersOfClick(item) }
            holder.binding.mainListItemRoot.setOnLongClickListener { notifyListenersOfLongClick(item) }
            holder.binding.executePendingBindings()
        }
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<MainListItemViewModel>() {
            override fun areContentsTheSame(oldItem: MainListItemViewModel, newItem: MainListItemViewModel): Boolean =
                    oldItem.pic == newItem.pic

            override fun areItemsTheSame(oldItem: MainListItemViewModel, newItem: MainListItemViewModel): Boolean =
                    oldItem == newItem
        }
    }
}