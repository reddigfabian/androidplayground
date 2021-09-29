package com.fabian.androidplayground.common.databinding

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.navigation.NavToInstructions
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.ui.onboarding.views.OnboardingFragment
import com.fabian.androidplayground.ui.user.login.views.LoginFragment
import kotlinx.coroutines.launch

abstract class AuthenticatedDataBindingFragment<T : ViewDataBinding>(@LayoutRes layoutRes : Int? = null) : BaseDataBindingFragment<T>(layoutRes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                checkAuthentication()
            }
        }
    }

    private suspend fun checkAuthentication() {
        if (!getViewModel().isOnboarded()) {
            Log.d(TAG, "onViewCreated: Not onboarded")
            val onboardingCancelled = !(findNavController().currentBackStackEntry?.savedStateHandle?.get<Boolean>(OnboardingFragment.ONBOARDING_COMPLETE) ?: true)
            if (onboardingCancelled) {
                requireActivity().finish()
            } else {
                findNavController().executeNavInstructions(NavToInstructions(R.id.to_onboarding))
            }
        } else if (!getViewModel().isLoggedIn()) {
            Log.d(TAG, "onViewCreated: Not logged in")
            val loginCancelled = !(findNavController().currentBackStackEntry?.savedStateHandle?.get<Boolean>(LoginFragment.LOGIN_COMPLETE) ?: true)
            if (loginCancelled) {
                requireActivity().finish()
            } else {
                findNavController().executeNavInstructions(NavToInstructions(R.id.to_login))
            }
        } else {
            Log.d(TAG, "onViewCreated: All good")
            onAuthenticationPassed()
        }
    }

    open fun onAuthenticationPassed() {
        goToNext()
    }
}