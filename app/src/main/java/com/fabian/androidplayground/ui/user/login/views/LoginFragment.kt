package com.fabian.androidplayground.ui.user.login.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.databinding.FragmentLoginBinding
import com.fabian.androidplayground.ui.user.login.viewmodels.LoginViewModel
import com.fabian.androidplayground.ui.user.password.views.ChoosePasswordFragment
import com.fabian.androidplayground.ui.user.password.views.ConfirmPasswordFragment

class LoginFragment : BaseDataBindingFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    companion object {
        private const val TAG = "LoginFragment"
        const val LOGIN_COMPLETE = "LOGIN_COMPLETE"
    }
    private val loginViewModel : LoginViewModel by viewModels {
        LoginViewModel.Factory(requireContext().dataStore)
    }

    override fun setDataBoundViewModels(binding: FragmentLoginBinding) {
        binding.loginViewModel = loginViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(LOGIN_COMPLETE, false)
    }

    override suspend fun startUpCheck() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel.loginComplete.observe(viewLifecycleOwner) { loginComplete ->
            findNavController().previousBackStackEntry?.savedStateHandle?.set(LOGIN_COMPLETE, loginComplete)
            if (loginComplete) {
                findNavController().executeNavInstructions(NavPopInstructions(R.id.LoginFragment, true))
            }
        }
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return loginViewModel
    }
}