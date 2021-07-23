package com.fabian.androidplayground.ui.main.state

import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.state.State
import com.fabian.androidplayground.common.state.ViewState

class MainState(viewState : ViewState = Launch,
                viewStateArgs : Bundle? = null,
                navOptions : NavOptions? = null,
                extras : FragmentNavigator.Extras? = null
) : State(viewState, viewStateArgs, navOptions, extras) {
    object Launch : ViewState(R.id.LaunchFragment)
    object Picsum : ViewState(R.id.LoremPicsumListFragment)
    object Finnhub : ViewState(R.id.FinnhubListFragment)
}