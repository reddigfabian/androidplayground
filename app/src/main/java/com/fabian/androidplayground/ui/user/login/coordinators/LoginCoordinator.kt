package com.fabian.androidplayground.ui.user.login.coordinators

import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions

object LoginCoordinator {
    var loginFinishedCallback : (Boolean) -> NavInstructions = {
        NavPopInstructions(R.id.login_nav_graph, true)
    }

    fun start(loginFinishedCallback : (Boolean) -> NavInstructions): NavInstructions {
        this.loginFinishedCallback = loginFinishedCallback
        return NavToInstructions(R.id.login_nav_graph)
    }

    fun finish(): NavInstructions {
        return loginFinishedCallback(true)
    }

    fun abort(): NavInstructions {
        return loginFinishedCallback(false)
    }
}