package com.fabian.androidplayground.ui.onboarding.views

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.ui.onboarding.viewmodels.OnboardingViewModel

class OnboardingFragment : BaseDataBindingFragment<ViewDataBinding>() {
    private val onboardingViewModel : OnboardingViewModel by viewModels {
        OnboardingViewModel.Factory(requireContext().dataStore)
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return onboardingViewModel
    }
}