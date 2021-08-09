package com.fabian.androidplayground.ui.main.launch.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.databinding.FragmentLaunchBinding
import com.fabian.androidplayground.ui.main.launch.viewmodels.LaunchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "LaunchFragment"

@FlowPreview
@ExperimentalCoroutinesApi
class LaunchFragment : BaseDataBindingFragment<FragmentLaunchBinding>(R.layout.fragment_launch) {
    private val launchViewModel : LaunchViewModel by viewModels()

    override fun setDataBoundViewModels(binding: FragmentLaunchBinding) {
        binding.launchViewModel = launchViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){ //This will collect when started and cancel the coroutine when stopped
                launchViewModel.navigationInstructions.collectLatest {
                    findNavController().executeNavInstructions(it)
                }
            }
        }
    }
}