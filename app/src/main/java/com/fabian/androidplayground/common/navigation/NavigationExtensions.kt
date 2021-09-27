package com.fabian.androidplayground.common.navigation

import android.util.Log
import androidx.navigation.NavController

private const val TAG = "NavigationExtensions"

fun NavController.executeNavInstructions(navInstructions : NavInstructions) : Boolean {
    return when (navInstructions) {
        is NavToInstructions -> {
            Log.d(TAG, "executeNavInstructions: got navTo instruction : ${navInstructions.navDestinationID}")
            navigate(navInstructions.navDestinationID, navInstructions.navArgs, navInstructions.navOptions, navInstructions.navigatorExtras)
            true
        }
        is NavBackInstruction -> {
            Log.d(TAG, "executeNavInstructions: got navBack instruction")
            val popped = popBackStack()
            Log.d(TAG, "executeNavInstructions: back | popped = $popped")
            popped
        }
        is NavPopInstructions -> {
            Log.d(TAG, "executeNavInstructions: got navPop instruction : ${navInstructions.navDestinationID}")
            val popped = popBackStack(navInstructions.navDestinationID, navInstructions.inclusive)
            Log.d(TAG, "executeNavInstructions: pop | popped = $popped")
            popped
        }
        else -> {
            false
        }
    }
}