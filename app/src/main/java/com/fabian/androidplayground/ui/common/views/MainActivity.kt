package com.fabian.androidplayground.ui.common.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingActivity
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.common.navigation.executeNavInstructions
import com.fabian.androidplayground.databinding.ActivityMainBinding
import com.fabian.androidplayground.ui.common.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : BaseDataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel : MainViewModel by viewModels {
        MainViewModel.Factory(dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIntent(intent)

        mainViewModel.getTitleResID().observe(this) { titleResID ->
            supportActionBar?.title = getString(titleResID)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mainViewModel.navigationInstructions.collect {
                    findNavController(R.id.navHostFragment).executeNavInstructions(it)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkIntent(intent)
    }

    private fun checkIntent(intent: Intent){
        when (intent.action) {
            Intent.ACTION_SEND,
            Intent.ACTION_SEND_MULTIPLE -> {

            }
            else -> {
                // Handle other intents, if needed
            }
        }
    }
}