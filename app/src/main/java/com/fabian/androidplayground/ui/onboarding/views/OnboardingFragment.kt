package com.fabian.androidplayground.ui.onboarding.views

import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.auth.LoginToken
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.ui.onboarding.viewmodels.OnboardingViewModel
import com.fabian.androidplayground.ui.user.name.views.ChooseNameFragment
import com.fabian.androidplayground.ui.user.password.views.ChoosePasswordFragment

class OnboardingFragment : BaseDataBindingFragment<ViewDataBinding>() {
    companion object {
        const val ONBOARDING_COMPLETE = "ONBOARDING_COMPLETE"
    }

    override val TAG = "OnboardingFragment"

    private val onboardingViewModel : OnboardingViewModel by viewModels {
        OnboardingViewModel.Factory(requireContext().dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(ONBOARDING_COMPLETE, false)
    }

    override suspend fun startUpCheck() : Boolean {
        Log.d(TAG, "startUpCheck: onboarding")
        return if (!onboardingViewModel.hasName().await()) {
            val chooseNameCancelled = !(findNavController().currentBackStackEntry?.savedStateHandle?.get<Boolean>(ChooseNameFragment.CHOOSE_NAME_COMPLETE) ?: true)
            if (chooseNameCancelled) {
                findNavController().executeNavInstructions(NavPopInstructions(R.id.onboarding_nav_graph, true))
            } else {
                findNavController().executeNavInstructions(NavToInstructions(R.id.to_choose_name))
            }
            true
        } else if (!onboardingViewModel.hasPassword().await()) {
            val choosePasswordCancelled = !(findNavController().currentBackStackEntry?.savedStateHandle?.get<Boolean>(ChoosePasswordFragment.CHOOSE_PASSWORD_COMPLETE) ?: true)
            if (choosePasswordCancelled) {
                findNavController().executeNavInstructions(NavPopInstructions(R.id.onboarding_nav_graph, true))
            } else {
                findNavController().executeNavInstructions(NavToInstructions(R.id.to_choose_password))
            }
            true
        } else {
            LoginToken.isLoggedIn = true
            findNavController().previousBackStackEntry?.savedStateHandle?.set(ONBOARDING_COMPLETE, true)
            findNavController().executeNavInstructions(NavPopInstructions(R.id.onboarding_nav_graph, true))
            true
        }
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return onboardingViewModel
    }
}