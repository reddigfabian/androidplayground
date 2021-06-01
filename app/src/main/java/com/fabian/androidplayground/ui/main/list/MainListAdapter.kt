package com.fabian.androidplayground.ui.main.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
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
class MainListAdapter : ItemClickPagingAdapter<Picsum>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_main_list, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position)?.let { picsum ->
            (holder.binding as ItemMainListBinding).mainListItemRoot.transitionName = "transition$position"
            MainListItemViewModel(picsum).toRecyclerItem().bind(holder.binding)
            holder.binding.mainListItemRoot.setOnClickListener { notifyListenersOfClick(picsum) }
            holder.binding.mainListItemRoot.setOnLongClickListener { notifyListenersOfLongClick(picsum) }
            holder.binding.executePendingBindings()
        }
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Picsum>() {
            override fun areContentsTheSame(oldItem: Picsum, newItem: Picsum): Boolean =
                    oldItem == newItem

            override fun areItemsTheSame(oldItem: Picsum, newItem: Picsum): Boolean =
                    oldItem.id == newItem.id
        }
    }
}