package com.fabian.androidplayground.common.auth

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.NavigationRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.fabian.androidplayground.common.databinding.BaseDataBindingActivity
import com.fabian.androidplayground.common.datastore.DataStoreUtil
import kotlinx.coroutines.launch

abstract class AuthActivity<T : ViewDataBinding>(@NavigationRes private val navGraphRes : Int, @LayoutRes layoutRes : Int) : BaseDataBindingActivity<T>(layoutRes) {

    abstract val navHostFragmentID : Int

    var graphSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                if (!DataStoreUtil.isOnboarded(this@AuthActivity)) {
                    findNavController()
                } else if (!DataStoreUtil.isLoggedIn()) {

                } else {

                }
            }
        }
    }

    private fun setGraphOnce() {
        if (!graphSet) {
            graphSet = true
            findNavController(navHostFragmentID).setGraph(navGraphRes)
        }
    }
}