package com.fabian.androidplayground.ui.user.login.views

import androidx.fragment.app.viewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.databinding.FragmentLoginBinding
import com.fabian.androidplayground.ui.user.login.viewmodels.LoginViewModel

class LoginFragment : BaseDataBindingFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val loginViewModel : LoginViewModel by viewModels {
        LoginViewModel.Factory(requireContext().dataStore)
    }

    override fun setDataBoundViewModels(binding: FragmentLoginBinding) {
        binding.loginViewModel = loginViewModel
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return loginViewModel
    }
}