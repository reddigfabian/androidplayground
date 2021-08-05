package com.fabian.androidplayground.ui.main.picsum.list.viewmodels

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.fabian.androidplayground.BR
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.viewmodels.RecyclerItem

class LoremPicsumItemViewModel(val picsum : Picsum) : ViewModel(), LifecycleObserver

fun LoremPicsumItemViewModel.toRecyclerItem() =
    RecyclerItem(
        data = this,
        variableId = BR.loremPicsumItemViewModel,
        layoutID = R.layout.item_lorem_picsum_list
    )
