package com.fabian.androidplayground.ui.splash.views

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.ui.splash.viewmodels.SplashViewModel

class SplashFragment : BaseDataBindingFragment<ViewDataBinding>() {
    private val args by navArgs<SplashFragmentArgs>()
    private val splashViewModel : SplashViewModel by viewModels {
        SplashViewModel.Factory(args.nextID, args.intentArgs, requireContext().dataStore)
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return splashViewModel
    }
}