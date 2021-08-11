package com.fabian.androidplayground.ui.main.flows.views

import androidx.fragment.app.viewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.databinding.FragmentCoroutinesBinding
import com.fabian.androidplayground.databinding.FragmentFlowsBinding
import com.fabian.androidplayground.ui.main.coroutines.viewmodels.CoroutinesViewModel
import com.fabian.androidplayground.ui.main.flows.viewmodels.FlowsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class FlowsFragment : BaseDataBindingFragment<FragmentFlowsBinding>(R.layout.fragment_flows) {
    private val flowsViewModel : FlowsViewModel by viewModels()

    override fun setDataBoundViewModels(binding: FragmentFlowsBinding) {
        binding.flowsViewModel = flowsViewModel
    }
}