package com.fabian.androidplayground.common.databinding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.navigation.IntentNavArgs
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.ui.onboarding.views.OnboardingFragment
import com.fabian.androidplayground.ui.user.login.views.LoginFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseDataBindingFragment<T : ViewDataBinding>(@LayoutRes private val layoutRes : Int? = null) : Fragment() {

    open val TAG : String = "BaseDataBindingFragment"

    private var _viewDataBinding : T? = null
    protected val binding get() = _viewDataBinding!!

    open fun setDataBoundViewModels(binding: T){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(getViewModel())
        lifecycleScope.launch {
            getViewModel().navigationInstructions.collectLatest {
                if (!findNavController().executeNavInstructions(it)) {
                    requireActivity().finish()
                }
            }
        }
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutRes?.let { nonNullLayoutRes ->
            _viewDataBinding = DataBindingUtil.inflate(inflater, nonNullLayoutRes, container, false)
            setDataBoundViewModels(binding)
            return binding.root
        }
        Log.d(TAG, "onCreateView: Returning headless fragment")
        return null
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _viewDataBinding = null
    }

    abstract fun getViewModel() : BaseFragmentViewModel

    open fun getNextID() : Int {
        return 0
    }

    open fun getNextIntentArgs() : IntentNavArgs? {
        return null
    }

    protected fun goToNext() {
        if (getNextID() == 0) {
            findNavController().executeNavInstructions(NavPopInstructions(R.id.onboarding_nav_graph, true))
        } else {
            val b = Bundle()
            b.putParcelable(IntentNavArgs.PARCEL_KEY, getNextIntentArgs())
            findNavController().executeNavInstructions(NavToInstructions(getNextID(), b))
        }
    }
}