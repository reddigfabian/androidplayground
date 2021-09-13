package com.fabian.androidplayground.common.databinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseDataBindingFragment<T : ViewDataBinding>(@LayoutRes private val layoutRes : Int) : Fragment() {

    private var _viewDataBinding : T? = null
    protected val binding get() = _viewDataBinding!!

    abstract fun setDataBoundViewModels(binding: T)

    private val backButtonCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handleBackButtonPressedFromFragment()
        }
    }

    protected open fun handleBackButtonPressedFromFragment(){
        backButtonCallback.isEnabled = false
        activity?.onBackPressed()
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewDataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        setDataBoundViewModels(binding)
        return binding.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _viewDataBinding = null
    }

}