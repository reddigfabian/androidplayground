package com.fabian.androidplayground.ui.user.password.views

import androidx.navigation.navGraphViewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.databinding.FragmentConfirmPasswordBinding
import com.fabian.androidplayground.ui.user.password.viewmodels.PasswordViewModel

class ConfirmPasswordFragment : BaseDataBindingFragment<FragmentConfirmPasswordBinding>(R.layout.fragment_confirm_password) {
    private val passwordViewModel : PasswordViewModel by navGraphViewModels(R.id.choose_password_nav_graph) {
        PasswordViewModel.Factory(requireContext().dataStore)
    }

    override fun setDataBoundViewModels(binding: FragmentConfirmPasswordBinding) {
        binding.passwordViewModel = passwordViewModel
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return passwordViewModel
    }
}