package com.fabian.androidplayground.ui.onboarding.views

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.auth.LoginToken
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.common.navigation.IntentNavArgs
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.ui.onboarding.viewmodels.OnboardingViewModel
import com.fabian.androidplayground.ui.splash.views.SplashFragmentArgs
import com.fabian.androidplayground.ui.user.name.views.ChooseNameFragment
import com.fabian.androidplayground.ui.user.password.views.ChoosePasswordFragment
import com.fabian.androidplayground.ui.user.password.views.ChoosePasswordFragmentArgs
import kotlinx.coroutines.launch

class OnboardingFragment : BaseDataBindingFragment<ViewDataBinding>() {
    companion object {
        const val ONBOARDING_COMPLETE = "ONBOARDING_COMPLETE"
    }

    override val TAG = "OnboardingFragment"

    private val args by navArgs<OnboardingFragmentArgs>()
    private val onboardingViewModel : OnboardingViewModel by viewModels {
        OnboardingViewModel.Factory(requireContext().dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(ONBOARDING_COMPLETE, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                pickName()
            }
        }
    }

    private fun pickName() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(ChooseNameFragment.CHOOSE_NAME_COMPLETE)?.observe(viewLifecycleOwner) { chooseNameComplete ->
            if (chooseNameComplete) {
                pickPassword()
            } else {
                findNavController().executeNavInstructions(NavPopInstructions(R.id.onboarding_nav_graph, true))
            }
        }
    }

    private fun pickPassword() {

    }

    override fun getViewModel(): BaseFragmentViewModel {
        return onboardingViewModel
    }

    override fun getNextID(): Int {
        return args.nextID
    }

    override fun getNextIntentArgs(): IntentNavArgs? {
        return args.intentArgs
    }
}