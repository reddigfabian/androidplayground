package com.fabian.androidplayground.common.recyclerview.views

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)