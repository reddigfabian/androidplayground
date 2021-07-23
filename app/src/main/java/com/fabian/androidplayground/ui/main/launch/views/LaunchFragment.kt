package com.fabian.androidplayground.ui.main.launch.views

import androidx.fragment.app.viewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.state.StateFragment
import com.fabian.androidplayground.databinding.FragmentLaunchBinding
import com.fabian.androidplayground.ui.main.launch.viewmodels.LaunchViewModel
import com.fabian.androidplayground.ui.main.state.MainState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@ExperimentalCoroutinesApi
class LaunchFragment : StateFragment<MainState, FragmentLaunchBinding>(R.layout.fragment_launch) {
    private val launchViewModel : LaunchViewModel by viewModels()

    override fun setDataBoundViewModels(binding: FragmentLaunchBinding) {
        binding.launchViewModel = launchViewModel
    }

    override fun getCurrentState() = launchViewModel.getState<MainState>()
    override fun getStateFlow() = launchViewModel.getStateAsFlow<MainState>()
    override fun onBackPressed() = launchViewModel.onBackPressed()
}