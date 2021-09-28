package com.fabian.androidplayground.ui.onboarding.views

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.ui.onboarding.viewmodels.OnboardingViewModel

class OnboardingFragment : BaseDataBindingFragment<ViewDataBinding>() {
    private val onboardingViewModel : OnboardingViewModel by viewModels {
        OnboardingViewModel.Factory(requireContext().dataStore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onboardingViewModel.onboardingSuccess.observe(viewLifecycleOwner) { success ->
            findNavController().previousBackStackEntry?.savedStateHandle?.set(OnboardingViewModel.ONBOARDING_SUCCESS, success)
        }
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return onboardingViewModel
    }
}