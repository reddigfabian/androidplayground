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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseDataBindingFragment<T : ViewDataBinding>(@LayoutRes private val layoutRes : Int) : Fragment() {

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

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewDataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        setDataBoundViewModels(binding)
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                getViewModel().onBackPressed()
            }

        })
        return binding.root
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