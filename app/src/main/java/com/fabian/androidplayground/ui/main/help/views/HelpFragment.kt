package com.fabian.androidplayground.ui.main.help.views

import androidx.fragment.app.activityViewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.databinding.FragmentHelpBinding
import com.fabian.androidplayground.ui.main.help.viewmodels.HelpViewModel

class HelpFragment : BaseDataBindingFragment<FragmentHelpBinding>(R.layout.fragment_help) {
    private val helpViewModel: HelpViewModel by activityViewModels()

    override fun setDataBoundViewModels(binding: FragmentHelpBinding) {
        binding.helpViewModel = helpViewModel
    }
}