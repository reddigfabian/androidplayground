package com.fabian.androidplayground.ui.user.password.views

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.databinding.FragmentConfirmPasswordBinding
import com.fabian.androidplayground.ui.user.password.viewmodels.PasswordViewModel

class ConfirmPasswordFragment : BaseDataBindingFragment<FragmentConfirmPasswordBinding>(R.layout.fragment_confirm_password) {
    override val TAG = "ConfirmPasswordFragment"

    companion object {
        const val CONFIRM_PASSWORD_COMPLETE = "CONFIRM_PASSWORD_COMPLETE"
    }

    private val passwordViewModel : PasswordViewModel by navGraphViewModels(R.id.choose_password_nav_graph) {
        PasswordViewModel.Factory(requireContext().dataStore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passwordViewModel.passwordConfirmed.observe(viewLifecycleOwner) { passwordConfirmed ->
            findNavController().previousBackStackEntry?.savedStateHandle?.set(CONFIRM_PASSWORD_COMPLETE, passwordConfirmed)
            if (passwordConfirmed) {
                findNavController().executeNavInstructions(NavPopInstructions(R.id.ConfirmPasswordFragment, true))
            }
        }
    }

    override fun setDataBoundViewModels(binding: FragmentConfirmPasswordBinding) {
        binding.passwordViewModel = passwordViewModel
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return passwordViewModel
    }
}