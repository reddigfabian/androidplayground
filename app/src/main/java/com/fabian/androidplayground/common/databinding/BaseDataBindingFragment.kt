package com.fabian.androidplayground.common.databinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseDataBindingFragment<T : ViewDataBinding>(@LayoutRes private val layoutRes:Int) : Fragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewDataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = this
        setDataBoundViewModels(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backButtonCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewDataBinding = null
    }

}