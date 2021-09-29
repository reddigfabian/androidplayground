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
import com.fabian.androidplayground.common.navigation.NavToInstructions
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.databinding.FragmentChoosePasswordBinding
import com.fabian.androidplayground.ui.user.password.viewmodels.PasswordViewModel

class ChoosePasswordFragment : BaseDataBindingFragment<FragmentChoosePasswordBinding>(R.layout.fragment_choose_password) {
    override val TAG = "ChoosePasswordFragment"

    companion object {
        const val CHOOSE_PASSWORD_COMPLETE = "CHOOSE_PASSWORD_COMPLETE"
    }

    private val passwordViewModel : PasswordViewModel by navGraphViewModels(R.id.choose_password_nav_graph) {
        PasswordViewModel.Factory(requireContext().dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(CHOOSE_PASSWORD_COMPLETE, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passwordViewModel.passwordEntered.observe(viewLifecycleOwner) { passwordEntered ->
            if (passwordEntered) {
                passwordViewModel.passwordEntered.postValue(false)
                findNavController().executeNavInstructions(NavToInstructions(R.id.choose_password_to_confirm_password))
            }
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(ConfirmPasswordFragment.CONFIRM_PASSWORD_COMPLETE)?.observe(viewLifecycleOwner) { confirmPasswordComplete ->
            if (confirmPasswordComplete) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(CHOOSE_PASSWORD_COMPLETE, true)
                findNavController().executeNavInstructions(NavPopInstructions(R.id.ChoosePasswordFragment, true))
            }
        }
    }

    override fun setDataBoundViewModels(binding: FragmentChoosePasswordBinding) {
        binding.passwordViewModel = passwordViewModel
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return passwordViewModel
    }
}