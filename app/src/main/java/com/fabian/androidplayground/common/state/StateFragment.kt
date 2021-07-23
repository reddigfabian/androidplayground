package com.fabian.androidplayground.common.state

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.ui.main.state.MainState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class StateFragment<R : State, T : ViewDataBinding>(@LayoutRes layoutRes : Int) : BaseDataBindingFragment<T>(layoutRes) {

    private var job : Job? = null

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        job?.cancel()
        job = lifecycleScope.launch {
            getStateFlow().collectLatest { state ->
                val navController = findNavController()
                if (state.viewState == ExitState) {
                    if (!navController.popBackStack()) {
                        requireActivity().finish()
                    }
                } else {
                    navController.currentDestination?.let {
                        if (it.id != state.viewState.destinationId) {
                            if (!navController.popBackStack(state.viewState.destinationId, false)) {
                                navController.navigate(
                                    state.viewState.destinationId,
                                    state.viewStateArgs,
                                    state.navOptions,
                                    state.extras
                                )
                            }
                        }
                    }
                }
            }
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onStop() {
        super.onStop()
        if (getCurrentState().viewState == ExitState) {
            job?.cancel()
            job = null
        }
    }

    abstract fun getCurrentState() : R
    abstract fun getStateFlow() : Flow<R>
    abstract fun onBackPressed()
}