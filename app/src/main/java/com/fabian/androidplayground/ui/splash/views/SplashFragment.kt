package com.fabian.androidplayground.ui.splash.views

import android.os.Bundle
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingFragment
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.common.navigation.IntentNavArgs
import com.fabian.androidplayground.common.navigation.NavToInstructions
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.ui.splash.viewmodels.SplashViewModel
import com.fabian.androidplayground.ui.user.name.views.ChooseNameFragment

class SplashFragment : BaseDataBindingFragment<ViewDataBinding>() {
    override val TAG = "SplashFragment"
    private val args by navArgs<SplashFragmentArgs>()
    private val splashViewModel : SplashViewModel by viewModels {
        SplashViewModel.Factory(requireContext().dataStore)
    }

    override fun getViewModel(): BaseFragmentViewModel {
        return splashViewModel
    }

    override fun getNextID(): Int {
        return args.nextID
    }

    override fun getNextIntentArgs(): IntentNavArgs? {
        return args.intentArgs
    }
}