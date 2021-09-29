package com.fabian.androidplayground.ui.user.name.views

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.databinding.FragmentChooseNameBinding
import com.fabian.androidplayground.ui.onboarding.views.OnboardingFragment
import com.fabian.androidplayground.ui.user.name.viewmodels.ChooseNameViewModel

class ChooseNameFragment : BaseDataBindingFragment<FragmentChooseNameBinding>(R.layout.fragment_choose_name) {

    override val TAG = "ChooseNameFragment"

    companion object {
        const val CHOOSE_NAME_COMPLETE = "CHOOSE_NAME_COMPLETE"
    }

    private val chooseNameViewModel : ChooseNameViewModel by navGraphViewModels(R.id.choose_name_nav_graph) {
        ChooseNameViewModel.Factory(requireContext().dataStore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseNameViewModel.chooseNameComplete.observe(viewLifecycleOwner) { chooseNameComplete ->
            findNavController().previousBackStackEntry?.savedStateHandle?.set(CHOOSE_NAME_COMPLETE, chooseNameComplete)
            if (chooseNameComplete) {
                findNavController().executeNavInstructions(NavBackInstruction)
            }
        }
    }

    override fun setDataBoundViewModels(binding: FragmentChooseNameBinding) {
        binding.chooseNameViewModel = chooseNameViewModel
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return chooseNameViewModel
    }
}