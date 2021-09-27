package com.fabian.androidplayground.ui.user.password.views

import androidx.navigation.navGraphViewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.databinding.FragmentChoosePasswordBinding
import com.fabian.androidplayground.ui.user.password.viewmodels.ChoosePasswordViewModel

class ChoosePasswordFragment : BaseDataBindingFragment<FragmentChoosePasswordBinding>(R.layout.fragment_choose_password) {
    private val choosePasswordViewModel : ChoosePasswordViewModel by navGraphViewModels(R.id.choose_password_nav_graph) {
        ChoosePasswordViewModel.Factory(requireContext().dataStore)
    }

    override fun setDataBoundViewModels(binding: FragmentChoosePasswordBinding) {
        binding.choosePasswordViewModel = choosePasswordViewModel
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return choosePasswordViewModel
    }
}