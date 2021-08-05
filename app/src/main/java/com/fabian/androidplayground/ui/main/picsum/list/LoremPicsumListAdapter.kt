package com.fabian.androidplayground.ui.main.picsum.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.DiffUtil
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.ItemClickPagingAdapter
import com.fabian.androidplayground.common.recyclerview.views.BindingViewHolder
import com.fabian.androidplayground.databinding.ItemLoremPicsumListBinding
import com.fabian.androidplayground.ui.main.picsum.list.viewmodels.LoremPicsumItemViewModel
import com.fabian.androidplayground.ui.main.picsum.list.viewmodels.toRecyclerItem
import kotlinx.coroutines.FlowPreview

@FlowPreview
class LoremPicsumListAdapter(private val lifecycleOwner: LifecycleOwner, viewModelProviderOwner: ViewModelStoreOwner) : ItemClickPagingAdapter<LoremPicsumItemViewModel>(ITEM_COMPARATOR) {

    fun setData(pagingData: PagingData<Picsum>) {
        submitData(lifecycleOwner.lifecycle, pagingData
            .map { picsum ->
                LoremPicsumItemViewModel(picsum)
            }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return BindingViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_lorem_picsum_list, parent, false))
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position)?.let { item ->
            (holder.binding as? ItemLoremPicsumListBinding)?.let { itemMainListBinding ->
                item.toRecyclerItem().bind(holder.binding)
                holder.lifecycle.addObserver(item)
                itemMainListBinding.loremPicsumListItemRoot.setOnClickListener { notifyListenersOfClick(item) }
                itemMainListBinding.loremPicsumListItemRoot.setOnLongClickListener { notifyListenersOfLongClick(item) }
                itemMainListBinding.executePendingBindings()
            }
        }
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<LoremPicsumItemViewModel>() {
            override fun areContentsTheSame(oldItem: LoremPicsumItemViewModel, newItem: LoremPicsumItemViewModel): Boolean =
                    oldItem.picsum == newItem.picsum

            override fun areItemsTheSame(oldItem: LoremPicsumItemViewModel, newItem: LoremPicsumItemViewModel): Boolean =
                    oldItem == newItem
        }
    }
}