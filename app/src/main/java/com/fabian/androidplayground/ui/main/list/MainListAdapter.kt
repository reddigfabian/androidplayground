package com.fabian.androidplayground.ui.main.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.databinding.BindingViewHolder
import com.fabian.androidplayground.databinding.ItemMainListBinding
import com.fabian.androidplayground.ui.main.list.viewmodels.MainListItemViewModel
import com.fabian.androidplayground.ui.main.list.viewmodels.toRecyclerItem
import kotlin.reflect.KFunction1

class MainListAdapter(private val itemClickFunction: KFunction1<@ParameterName(name = "picsum") Pair<View, Picsum>, Unit>) : PagingDataAdapter<Picsum, BindingViewHolder>(ITEM_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.item_main_list, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position)?.let {
            (holder.binding as ItemMainListBinding).mainListItemRoot.transitionName = "transition$position"
            MainListItemViewModel(it, itemClickFunction).toRecyclerItem().bind(holder.binding)
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