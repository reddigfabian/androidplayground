package com.fabian.androidplayground.common.recyclerview.views

import android.animation.Animator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.fabian.androidplayground.R
import com.fabian.androidplayground.databinding.ItemRecyclerStateBinding

class RecyclerStateItemViewHolder(
        parent: ViewGroup,
        private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_state, parent, false)
) {
    private val binding = ItemRecyclerStateBinding.bind(itemView)
    private val loadingAnimationView = binding.loadingAnimationView
    private val errorMsg = binding.errorMsg
    private val retry = binding.retryButton
            .also {
                it.setOnClickListener { retryCallback() }
            }

    fun bindTo(loadState: LoadState) {
        when (loadState) {
            is LoadState.NotLoading -> {
                loadingAnimationView.addAnimatorListener(object : Animator.AnimatorListener{
                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        loadingAnimationView.setAnimation(R.raw.loadingpulsefadeout)
                        loadingAnimationView.playAnimation()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                })
                loadingAnimationView.repeatCount = 0
            }
            LoadState.Loading -> {}
            is LoadState.Error -> {}
            else -> {
            }
        }
        loadingAnimationView.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
        errorMsg.text = (loadState as? LoadState.Error)?.error?.message
    }
}