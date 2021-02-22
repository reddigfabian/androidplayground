package com.fabian.androidplayground.ui.main.list.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import com.fabian.androidplayground.R
import com.fabian.androidplayground.BR
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.RecyclerItem

class MainListItemViewModel(val pic: Picsum, private val itemClickFunction: (pic: Pair<View, Picsum>) -> Unit) : ViewModel() {
    fun onClick(view : View) {
        itemClickFunction(view to pic)
    }
}

fun MainListItemViewModel.toRecyclerItem() =
    RecyclerItem(
        data = this,
        variableId = BR.mainListItemViewModel,
        layoutID = R.layout.item_main_list
    )