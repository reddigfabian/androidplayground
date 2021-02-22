package com.fabian.androidplayground.common.recyclerview

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

data class RecyclerItem(
    val data: Any,
    @LayoutRes val layoutID: Int,
    val variableId: Int
) {
    fun bind(binding: ViewDataBinding) {
        if (variableId != 0) {
            binding.setVariable(variableId, data)
        }
    }
}