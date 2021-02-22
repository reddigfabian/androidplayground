package com.fabian.androidplayground.ui.main.views

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseDataBindingActivity
import com.fabian.androidplayground.databinding.ActivityMainBinding
import com.fabian.androidplayground.ui.main.detail.viewmodels.DetailViewModel
import com.fabian.androidplayground.ui.main.list.viewmodels.ListViewModel
import com.fabian.androidplayground.ui.main.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : BaseDataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel : MainViewModel by viewModels()
    private val listViewModel : ListViewModel by viewModels()
    private val detailVieWModel : DetailViewModel by viewModels()
    private lateinit var navController : NavController

    override fun onStart() {
        super.onStart()

        navController = findNavController(this, R.id.navHostFragment)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            mainViewModel.handleNavigationDestinationChange(destination.id)
        }

        mainViewModel.getTitleResID().observe(this) { titleResID ->
            supportActionBar?.title = getString(titleResID)
        }

        lifecycleScope.launch {
            mainViewModel.mainStateAsFlow().collectLatest { mainState ->
                if (mainState.viewState.destinationId != navController.graph.startDestination) {
                    if (!navController.popBackStack(mainState.viewState.destinationId, false)) {
                        navController.navigate(mainState.viewState.destinationId, mainState.viewStateArgs, mainState.navOptions, mainState.extras)
                    }
                }
            }
        }

        lifecycleScope.launch {
            val viewModelClickEventFlows = listOf(
                detailVieWModel.getClickEventFlow()
            )
            viewModelClickEventFlows.merge().collect {
                mainViewModel.onClick(it)
            }
        }

        lifecycleScope.launch {
            listViewModel.getItemSelectedFlow().collectLatest {
                mainViewModel.onListItemSelected(it)
            }
        }
    }
}