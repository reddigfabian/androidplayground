package com.fabian.androidplayground.ui.user.name.views

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.common.navigation.IntentNavArgs
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.databinding.FragmentChooseNameBinding
import com.fabian.androidplayground.ui.onboarding.views.OnboardingFragment
import com.fabian.androidplayground.ui.onboarding.views.OnboardingFragmentArgs
import com.fabian.androidplayground.ui.user.name.viewmodels.ChooseNameViewModel
import com.fabian.androidplayground.ui.user.password.views.ChoosePasswordFragmentArgs
import kotlinx.coroutines.launch

class ChooseNameFragment : BaseDataBindingFragment<FragmentChooseNameBinding>(R.layout.fragment_choose_name) {

    override val TAG = "ChooseNameFragment"

    companion object {
        const val CHOOSE_NAME_COMPLETE = "CHOOSE_NAME_COMPLETE"
        const val CHOSEN_NAME = "CHOSEN_NAME"
    }

    private val args by navArgs<ChoosePasswordFragmentArgs>()
    private val chooseNameViewModel : ChooseNameViewModel by navGraphViewModels(R.id.choose_name_nav_graph) {
        ChooseNameViewModel.Factory(requireContext().dataStore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseNameViewModel.chooseNameComplete.observe(viewLifecycleOwner) { chooseNameComplete ->
            if (chooseNameComplete) {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(CHOSEN_NAME, chooseNameViewModel.name.value)
                if (getNextID() == 0) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        if (chooseNameViewModel.submitNameAsync(chooseNameViewModel.name.value).await()) {
                            goToNext()
                        } else {
                            Toast.makeText(requireContext(), "Something went wrong :(", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    goToNext()
                }
            } else {
                findNavController().previousBackStackEntry?.savedStateHandle?.set(CHOOSE_NAME_COMPLETE, chooseNameComplete)
            }
        }
    }

    override fun setDataBoundViewModels(binding: FragmentChooseNameBinding) {
        binding.chooseNameViewModel = chooseNameViewModel
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return chooseNameViewModel
    }

    override fun getNextID(): Int {
        return args.nextID
    }

    override fun getNextIntentArgs(): IntentNavArgs? {
        return args.intentArgs
    }
}