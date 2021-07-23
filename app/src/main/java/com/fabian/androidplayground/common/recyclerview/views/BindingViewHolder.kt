package com.fabian.androidplayground.common.recyclerview.views

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private var paused: Boolean = false

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        binding.lifecycleOwner = this
        lifecycleCreate()
    }

    private fun lifecycleCreate() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        paused = false
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    fun attachToWindow() {
        if (paused) {
            lifecycleRegistry.currentState = Lifecycle.State.RESUMED
            paused = false
        } else {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }
    }

    fun detachFromWindow() {
        if (paused) {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        } else {
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
            paused = true
        }
    }
}