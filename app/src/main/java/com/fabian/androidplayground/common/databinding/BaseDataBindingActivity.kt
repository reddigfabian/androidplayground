package com.fabian.androidplayground.common.databinding

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

open class BaseDataBindingActivity<T : ViewDataBinding>(@LayoutRes private val layoutRes:Int) : AppCompatActivity() {

    private var _viewDataBinding : T? = null
    protected val binding get() = _viewDataBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewDataBinding = DataBindingUtil.setContentView(this, layoutRes)
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewDataBinding = null
    }
}