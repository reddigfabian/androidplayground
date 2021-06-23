package com.fabian.androidplayground.ui.main.launch.views

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.databinding.FragmentLaunchBinding
import com.fabian.androidplayground.ui.main.launch.viewmodels.LaunchViewModel
import kotlinx.android.synthetic.main.fragment_launch.*

class LaunchFragment : BaseDataBindingFragment<FragmentLaunchBinding>(R.layout.fragment_launch) {
    private val launchViewModel : LaunchViewModel by activityViewModels()

    override fun setDataBoundViewModels(binding: FragmentLaunchBinding) {
        binding.launchVieWModel = launchViewModel
    }
}