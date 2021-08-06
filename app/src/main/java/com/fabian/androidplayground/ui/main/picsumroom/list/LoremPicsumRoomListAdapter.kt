package com.fabian.androidplayground.ui.main.picsumroom.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DiffUtil
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.ItemClickVMProviderPagingAdapter
import com.fabian.androidplayground.common.recyclerview.views.BindingViewHolder
import com.fabian.androidplayground.databinding.ItemLoremPicsumListBinding
import com.fabian.androidplayground.databinding.ItemLoremPicsumRoomListBinding
import com.fabian.androidplayground.ui.main.picsumroom.list.viewmodels.LoremPicsumRoomItemViewModel
import com.fabian.androidplayground.ui.main.picsumroom.list.viewmodels.toRecyclerItem
import kotlinx.coroutines.FlowPreview

@FlowPreview
class LoremPicsumRoomListAdapter(private val lifecycleOwner: LifecycleOwner, viewModelProviderOwner: ViewModelStoreOwner) : ItemClickVMProviderPagingAdapter<Picsum>(viewModelProviderOwner, ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return BindingViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_lorem_picsum_room_list, parent, false))
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position)?.let { picsum ->
            (holder.binding as? ItemLoremPicsumRoomListBinding)?.let { itemMainListBinding ->
//                val item = LoremPicsumRoomItemViewModel()
//                item.setData(picsum)
                val item = provideViewModelItem<LoremPicsumRoomItemViewModel>(picsum.id, picsum)
                item.toRecyclerItem().bind(holder.binding)
//                holder.lifecycle.addObserver(item)
                itemMainListBinding.loremPicsumListItemRoot.setOnClickListener { notifyListenersOfClick(picsum) }
                itemMainListBinding.loremPicsumListItemRoot.setOnLongClickListener { notifyListenersOfLongClick(picsum) }
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