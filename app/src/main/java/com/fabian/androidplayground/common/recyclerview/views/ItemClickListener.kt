package com.fabian.androidplayground.common.recyclerview.views

import android.view.View

interface ItemClickListener<T> {
    fun onItemClick(item : T, view : View)
    fun onItemLongClick(item : T, view : View)
}