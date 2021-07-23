package com.fabian.androidplayground.ui.main.launch.views

import androidx.fragment.app.viewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.databinding.FragmentLaunchBinding
import com.fabian.androidplayground.ui.main.launch.viewmodels.LaunchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class LaunchFragment : BaseDataBindingFragment<FragmentLaunchBinding>(R.layout.fragment_launch) {
    private val launchViewModel : LaunchViewModel by viewModels()

    override fun setDataBoundViewModels(binding: FragmentLaunchBinding) {
        binding.launchViewModel = launchViewModel
    }
}