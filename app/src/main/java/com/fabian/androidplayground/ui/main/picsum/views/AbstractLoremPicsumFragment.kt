package com.fabian.androidplayground.ui.main.picsum.views

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.navigation.navGraphViewModels
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.state.StateFragment
import com.fabian.androidplayground.databinding.FragmentLoremPicsumListBinding
import com.fabian.androidplayground.ui.main.picsum.state.LoremPicsumState
import com.fabian.androidplayground.ui.main.picsum.viewmodels.LoremPicsumViewModel
import com.fabian.androidplayground.ui.main.state.MainState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@ExperimentalCoroutinesApi
abstract class AbstractLoremPicsumFragment<T : ViewDataBinding>(@LayoutRes layoutRes : Int) : StateFragment<LoremPicsumState, T>(layoutRes) {
    private val loremPicsumViewModel : LoremPicsumViewModel by navGraphViewModels(R.id.main_nav_graph)

    override fun getCurrentState() = loremPicsumViewModel.getState<LoremPicsumState>()
    override fun getStateFlow() = loremPicsumViewModel.getStateAsFlow<LoremPicsumState>()
    override fun onBackPressed() = loremPicsumViewModel.onBackPressed()
}