package com.fabian.androidplayground.common.navigation

import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

open class NavInstructions(val navDestinationID : Int,
                           val navArgs : Bundle? = null,
                           val navOptions : NavOptions? = null,
                           val navigatorExtras : Navigator.Extras? = null)

object NavBackInstruction : NavInstructions(0)