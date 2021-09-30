package com.fabian.androidplayground.ui.user.password.coordinators

import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions

object PasswordCoordinator {
    lateinit var passwordFinishedCallback : (Boolean, String?) -> NavInstructions

    fun start(passwordFinishedCallback : (Boolean, String?) -> NavInstructions): NavInstructions {
        this.passwordFinishedCallback = passwordFinishedCallback
        return NavToInstructions(R.id.choose_password_nav_graph)
    }

    fun navigateToConfirmPassword() : NavInstructions {
        return NavToInstructions(R.id.ConfirmPasswordFragment)
    }

    fun finish(password : String) : NavInstructions {
        return passwordFinishedCallback(true, password)
    }

    fun abort() : NavInstructions {
        return passwordFinishedCallback(false, null)
    }
}