package com.fabian.androidplayground.ui.main.picsum.state

import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.state.State
import com.fabian.androidplayground.common.state.ViewState

class LoremPicsumState(viewState : ViewState = List,
                viewStateArgs : Bundle? = null,
                navOptions : NavOptions? = null,
                extras : FragmentNavigator.Extras? = null
) : State(viewState, viewStateArgs, navOptions, extras) {
    object List : ViewState(R.id.LoremPicsumListFragment)
    object Detail : ViewState(R.id.LoremPicsumDetailFragment)
}