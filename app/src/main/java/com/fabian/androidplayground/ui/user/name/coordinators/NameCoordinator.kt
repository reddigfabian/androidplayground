package com.fabian.androidplayground.ui.user.name.coordinators

import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions

object NameCoordinator {
    lateinit var nameFinishedCallback : (Boolean, String?) -> NavInstructions

    fun start(nameFinishedCallback : (Boolean, String?) -> NavInstructions): NavInstructions {
        this.nameFinishedCallback = nameFinishedCallback
        return NavToInstructions(R.id.choose_name_nav_graph)
    }

    fun finish(name : String) : NavInstructions {
        return nameFinishedCallback(true, name)
    }

    fun abort() : NavInstructions {
        return nameFinishedCallback(false, null)
    }
}