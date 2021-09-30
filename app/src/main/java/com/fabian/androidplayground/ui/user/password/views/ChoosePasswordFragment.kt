package com.fabian.androidplayground.ui.user.password.views

import androidx.navigation.navGraphViewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.databinding.FragmentChoosePasswordBinding
import com.fabian.androidplayground.ui.user.password.viewmodels.PasswordViewModel

class ChoosePasswordFragment : BaseDataBindingFragment<FragmentChoosePasswordBinding>(R.layout.fragment_choose_password) {
    override val TAG = "ChoosePasswordFragment"

    private val passwordViewModel : PasswordViewModel by navGraphViewModels(R.id.choose_password_nav_graph) {
        PasswordViewModel.Factory(requireContext().dataStore)
    }

    override fun setDataBoundViewModels(binding: FragmentChoosePasswordBinding) {
        binding.passwordViewModel = passwordViewModel
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return passwordViewModel
    }
}