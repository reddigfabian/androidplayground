package com.fabian.androidplayground.ui.main.picsumroom.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.ItemClickPagingAdapter
import com.fabian.androidplayground.common.recyclerview.views.BindingViewHolder
import com.fabian.androidplayground.databinding.ItemLoremPicsumRoomListBinding
import kotlinx.coroutines.FlowPreview

@FlowPreview
class LoremPicsumRoomListAdapter : ItemClickPagingAdapter<Picsum>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return BindingViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_lorem_picsum_room_list, parent, false))
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position)?.let { picsum ->
            (holder.binding as? ItemLoremPicsumRoomListBinding)?.let { itemMainListBinding ->
                picsum.toRecyclerItem().bind(holder.binding)
                itemMainListBinding.mainListItemCard.setOnClickListener { v -> notifyListenersOfClick(picsum, v) }
                itemMainListBinding.mainListItemCard.setOnLongClickListener { v -> notifyListenersOfLongClick(picsum, v) }
                itemMainListBinding.executePendingBindings()
            }
        }
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Picsum>() {
            override fun areContentsTheSame(oldItem: Picsum, newItem: Picsum): Boolean = oldItem.id == newItem.id
            override fun areItemsTheSame(oldItem: Picsum, newItem: Picsum): Boolean = oldItem == newItem
        }
    }
}