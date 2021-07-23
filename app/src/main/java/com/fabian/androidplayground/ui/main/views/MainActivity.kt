package com.fabian.androidplayground.ui.main.views

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingActivity
import com.fabian.androidplayground.databinding.ActivityMainBinding
import com.fabian.androidplayground.ui.main.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : BaseDataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel : MainViewModel by viewModels()

    override fun onStart() {
        super.onStart()

        mainViewModel.getTitleResID().observe(this) { titleResID ->
            supportActionBar?.title = getString(titleResID)
        }
    }
}