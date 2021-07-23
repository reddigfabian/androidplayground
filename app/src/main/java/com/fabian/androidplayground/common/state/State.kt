package com.fabian.androidplayground.common.state

import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator

abstract class State(val viewState : ViewState,
                val viewStateArgs : Bundle? = null,
                val navOptions : NavOptions? = null,
                val extras : FragmentNavigator.Extras? = null
)

abstract class ViewState(val destinationId : Int)
object DestroyState : ViewState(0)
object PauseState : ViewState(0)