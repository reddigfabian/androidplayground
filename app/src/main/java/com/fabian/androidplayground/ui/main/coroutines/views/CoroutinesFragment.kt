package com.fabian.androidplayground.ui.main.coroutines.views

import androidx.fragment.app.viewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.databinding.FragmentCoroutinesBinding
import com.fabian.androidplayground.ui.main.coroutines.viewmodels.CoroutinesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class CoroutinesFragment : BaseDataBindingFragment<FragmentCoroutinesBinding>(R.layout.fragment_coroutines) {
    private val coroutinesViewModel : CoroutinesViewModel by viewModels()

    override fun setDataBoundViewModels(binding: FragmentCoroutinesBinding) {
        binding.coroutinesViewModel = coroutinesViewModel
    }
}