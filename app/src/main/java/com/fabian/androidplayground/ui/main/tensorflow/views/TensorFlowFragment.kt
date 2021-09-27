package com.fabian.androidplayground.ui.main.tensorflow.views

import androidx.fragment.app.viewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.databinding.FragmentTensorflowBinding
import com.fabian.androidplayground.ui.main.tensorflow.viewmodels.TensorFlowViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class TensorFlowFragment : BaseDataBindingFragment<FragmentTensorflowBinding>(R.layout.fragment_tensorflow) {
    private val tensorFlowViewModel : TensorFlowViewModel by viewModels {
        TensorFlowViewModel.Factory(requireContext().dataStore)
    }

    override fun setDataBoundViewModels(binding: FragmentTensorflowBinding) {
        binding.tensorFlowViewModel = tensorFlowViewModel
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return tensorFlowViewModel
    }
}