package com.fabian.androidplayground.ui.onboarding.coordinators

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.ui.common.coordinators.MainCoordinator
import com.fabian.androidplayground.ui.user.name.coordinators.NameCoordinator
import com.fabian.androidplayground.ui.user.password.coordinators.PasswordCoordinator

object OnboardingCoordinator {
    lateinit var onboardingFinishedCallback : (Boolean) -> NavInstructions

    fun start(dataStore: DataStore<Preferences>, onboardingFinishedCallback : (Boolean) -> NavInstructions): NavInstructions {
        this.onboardingFinishedCallback = onboardingFinishedCallback
        return displayNameModule { nameSuccess, chosenName ->
            if (nameSuccess && !chosenName.isNullOrEmpty()) {
                displayPasswordModule { passwordChosen, chosenPassword ->
                    if (passwordChosen && !chosenPassword.isNullOrEmpty()) {
                        if (MainCoordinator.submitName(dataStore, chosenName) && MainCoordinator.submitPassword(dataStore, chosenPassword)) {
                            finish()
                        } else {
                            start(dataStore, onboardingFinishedCallback) // TODO: 9/30/21 better error handling
                        }
                    } else {
                        abort()
                    }
                }
            } else {
                abort()
            }
        }
    }

    private fun displayNameModule(nameFinishedCallback : (Boolean, String?) -> NavInstructions): NavInstructions {
        return NameCoordinator.start(nameFinishedCallback)
    }

    private fun displayPasswordModule(passwordFinishedCallback : (Boolean, String?) -> NavInstructions): NavInstructions {
        return PasswordCoordinator.start(passwordFinishedCallback)
    }

    fun finish(): NavInstructions {
        return onboardingFinishedCallback(true)
    }

    fun abort(): NavInstructions {
        return onboardingFinishedCallback(false)
    }

}