package com.fabian.androidplayground.ui.main.picsumroom.list.viewmodels

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabian.androidplayground.BR
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.viewmodels.ItemViewModel
import com.fabian.androidplayground.common.recyclerview.viewmodels.RecyclerItem

class LoremPicsumRoomItemViewModel : ItemViewModel<Picsum>(), LifecycleObserver {

    val picsum = MutableLiveData<Picsum>()

    override fun setData(data: Picsum) {
        picsum.value = data
    }

}

fun LoremPicsumRoomItemViewModel.toRecyclerItem() =
    RecyclerItem(
        data = this,
        variableId = BR.loremPicsumItemViewModel,
        layoutID = R.layout.item_lorem_picsum_room_list
    )
