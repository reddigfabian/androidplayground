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
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "BaseDataBindingFragment"

abstract class BaseDataBindingFragment<T : ViewDataBinding>(@LayoutRes private val layoutRes : Int? = null) : Fragment() {

    private var _viewDataBinding : T? = null
    protected val binding get() = _viewDataBinding!!

    open fun setDataBoundViewModels(binding: T){}

    private val backButtonCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            handleBackButtonPressedFromFragment()
        }
    }

    protected open fun handleBackButtonPressedFromFragment(){
        backButtonCallback.isEnabled = false
        activity?.onBackPressed()
    }

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

}