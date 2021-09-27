package com.fabian.androidplayground.ui.user.name.views

import androidx.navigation.navGraphViewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.databinding.FragmentChooseNameBinding
import com.fabian.androidplayground.ui.user.name.viewmodels.ChooseNameViewModel

class ChooseNameFragment : BaseDataBindingFragment<FragmentChooseNameBinding>(R.layout.fragment_choose_name) {
    private val chooseNameViewModel : ChooseNameViewModel by navGraphViewModels(R.id.choose_name_nav_graph) {
        ChooseNameViewModel.Factory(requireContext().dataStore)
    }

    override fun setDataBoundViewModels(binding: FragmentChooseNameBinding) {
        binding.chooseNameViewModel = chooseNameViewModel
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return chooseNameViewModel
    }
}