package com.fabian.androidplayground.common.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.fabian.androidplayground.R
import com.fabian.androidplayground.databinding.ItemNetworkStateBinding

class NetworkStateItemViewHolder(
        parent: ViewGroup,
        private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_network_state, parent, false)
) {
    private val binding = ItemNetworkStateBinding.bind(itemView)
    private val progressBar = binding.progressBar
    private val errorMsg = binding.errorMsg
    private val retry = binding.retryButton
            .also {
                it.setOnClickListener { retryCallback() }
            }

    fun bindTo(loadState: LoadState) {
//        CustomizationUtils.subscribeToOptions(object : CustomizationUtils.OnCustomizationChangeListener {
//            override fun onCustomizationChange(option: CustomizationOption) {
//                when (option.id) {
//                    FontColor -> {
//                        val fontColor = option.value as Int
//                        val filter = SimpleColorFilter(fontColor)
//                        val keyPath = KeyPath("**")
//                        val callback = LottieValueCallback<ColorFilter>(filter)
//                        errorMsg.setTextColor(fontColor)
//                        progressBar.addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)
//                        retry.setTextColor(fontColor)
//                    }
//                    KeyColor,
//                    PressedKeyColor -> {
//                        retry.background = CustomizationUtils.getKeyBackgroundDrawable()
//                    }
//                    else -> {}
//                }
//            }
//        }, FontColor, KeyColor, PressedKeyColor)
        progressBar.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
        errorMsg.text = (loadState as? LoadState.Error)?.error?.message
    }
}