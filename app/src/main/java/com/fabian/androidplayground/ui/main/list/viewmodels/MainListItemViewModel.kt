package com.fabian.androidplayground.ui.main.list.viewmodels

import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.fabian.androidplayground.BR
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.recyclerview.viewmodels.RecyclerItem

class MainListItemViewModel(val pic: Picsum) : ViewModel() {
        val testOne : (View)-> Unit = { view ->
                Toast.makeText(view.context, "TestOne", Toast.LENGTH_SHORT).show()
        }

        val testTwo : (View)-> Unit = { view ->
                Toast.makeText(view.context, "TestTwo", Toast.LENGTH_SHORT).show()
        }

        val testFunctions = listOf(testOne, testTwo)
}

fun MainListItemViewModel.toRecyclerItem() =
        RecyclerItem(
                data = this,
                variableId = BR.mainListItemViewModel,
                layoutID = R.layout.item_main_list
        )