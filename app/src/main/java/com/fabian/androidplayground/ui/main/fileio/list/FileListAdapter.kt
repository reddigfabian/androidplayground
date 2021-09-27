package com.fabian.androidplayground.ui.main.fileio.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.recyclerview.ItemClickPagingAdapter
import com.fabian.androidplayground.common.recyclerview.views.BindingViewHolder
import com.fabian.androidplayground.databinding.ItemFileListBinding
import com.fabian.androidplayground.databinding.ItemLoremPicsumRoomListBinding
import com.fabian.androidplayground.ui.main.fileio.list.viewmodels.FileItemViewModel
import kotlinx.coroutines.FlowPreview

@FlowPreview
class FileListAdapter : ItemClickPagingAdapter<FileItemViewModel>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return BindingViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_file_list, parent, false))
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        getItem(position)?.let { fileItem ->
            (holder.binding as? ItemFileListBinding)?.let { itemFileListBinding ->
                fileItem.toRecyclerItem().bind(holder.binding)
                itemFileListBinding.fileItemRoot.setOnClickListener { v -> notifyListenersOfClick(fileItem, v) }
                itemFileListBinding.fileItemRoot.setOnLongClickListener { v -> notifyListenersOfLongClick(fileItem, v) }
                itemFileListBinding.executePendingBindings()
            }
        }
    }

    companion object {
        val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<FileItemViewModel>() {
            override fun areContentsTheSame(oldItem: FileItemViewModel, newItem: FileItemViewModel): Boolean = oldItem.uri == newItem.uri
            override fun areItemsTheSame(oldItem: FileItemViewModel, newItem: FileItemViewModel): Boolean = oldItem == newItem
        }
    }
}