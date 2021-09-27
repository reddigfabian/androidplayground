package com.fabian.androidplayground.common.navigation

import android.os.Bundle
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

abstract class NavInstructions(val navDestinationID : Int)

open class NavToInstructions(navDestinationID : Int,
                             val navArgs : Bundle? = null,
                             val navOptions : NavOptions? = null,
                             val navigatorExtras : Navigator.Extras? = null) : NavInstructions(navDestinationID)

open class NavPopInstructions(popToID : Int?, val inclusive : Boolean) : NavInstructions(popToID?:0)

object NavBackInstruction : NavPopInstructions(null, false)