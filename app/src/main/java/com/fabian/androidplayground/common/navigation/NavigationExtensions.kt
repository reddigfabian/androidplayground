package com.fabian.androidplayground.common.navigation

import androidx.navigation.NavController

fun NavController.executeNavInstructions(navInstructions : NavInstructions) {
    navigate(navInstructions.navDestinationID, navInstructions.navArgs, navInstructions.navOptions, navInstructions.navigatorExtras)
}