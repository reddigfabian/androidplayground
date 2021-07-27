package com.fabian.androidplayground.ui.main.tensorflow.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.databinding.FragmentLaunchBinding
import com.fabian.androidplayground.databinding.FragmentTensorflowBinding
import com.fabian.androidplayground.ui.main.launch.viewmodels.LaunchViewModel
import com.fabian.androidplayground.ui.main.tensorflow.viewmodels.TensorFlowViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class TensorFlowFragment : BaseDataBindingFragment<FragmentTensorflowBinding>(R.layout.fragment_tensorflow) {
    private val tensorFlowViewModel : TensorFlowViewModel by viewModels()


    override fun setDataBoundViewModels(binding: FragmentTensorflowBinding) {
        binding.tensorFlowViewModel = tensorFlowViewModel
    }
}