package com.fabian.androidplayground.ui.common.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingActivity
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.common.navigation.IntentNavArgs
import com.fabian.androidplayground.common.utils.IntentUtils
import com.fabian.androidplayground.databinding.ActivityMainBinding
import com.fabian.androidplayground.ui.common.viewmodels.MainViewModel
import com.fabian.androidplayground.ui.main.fileio.list.views.FileIOFragmentArgs
import com.fabian.androidplayground.ui.splash.views.SplashFragmentArgs
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

private const val TAG = "MainActivity"

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : BaseDataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel : MainViewModel by viewModels {
        MainViewModel.Factory(dataStore)
    }

    var nextID = R.id.to_launch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIntent(intent)

        mainViewModel.getTitleResID().observe(this) { titleResID ->
            supportActionBar?.title = getString(titleResID)
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
                nextID = R.id.to_fileio
            }
            else -> {
                // Handle other intents, if needed
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setGraphOnce()
    }

    var graphSet = false

    private fun setGraphOnce() {
        if (!graphSet) {
            graphSet = true
            findNavController(R.id.navHostFragment).setGraph(R.navigation.main_nav_graph, SplashFragmentArgs(nextID, IntentNavArgs.fromIntent(intent)).toBundle())
        }
    }
}