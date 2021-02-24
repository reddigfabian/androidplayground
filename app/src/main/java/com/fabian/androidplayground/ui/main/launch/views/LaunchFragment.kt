package com.fabian.androidplayground.ui.main.launch.views

import androidx.fragment.app.activityViewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.databinding.FragmentLaunchBinding
import com.fabian.androidplayground.ui.main.launch.viewmodels.LaunchViewModel

class LaunchFragment : BaseDataBindingFragment<FragmentLaunchBinding>(R.layout.fragment_launch) {
    private val launchViewModel : LaunchViewModel by activityViewModels()

    override fun setDataBoundViewModels(binding: FragmentLaunchBinding) {
        binding.launchVieWModel = launchViewModel
    }
}