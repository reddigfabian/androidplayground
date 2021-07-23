package com.fabian.androidplayground.ui.main.finnhub.list.viewmodels

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.fabian.androidplayground.BR
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.viewmodels.RecyclerItem
import com.fabian.androidplayground.ui.main.picsum.list.viewmodels.LoremPicsumItemViewModel

class FinnhubItemViewModel(val pic: Picsum) : ViewModel(), LifecycleObserver {

}

fun FinnhubItemViewModel.toRecyclerItem() =
        RecyclerItem(
                data = this,
                variableId = BR.loremPicsumItemViewModel,
                layoutID = R.layout.item_finnhub_list
        )