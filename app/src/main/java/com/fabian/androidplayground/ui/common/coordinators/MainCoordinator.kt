package com.fabian.androidplayground.ui.common.coordinators

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.auth.LoginToken
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions
import com.fabian.androidplayground.ui.onboarding.coordinators.OnboardingCoordinator
import com.fabian.androidplayground.ui.user.login.coordinators.LoginCoordinator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.coroutineContext

object MainCoordinator {

    private val namePref = stringPreferencesKey("NAME_PREF")
    private val passwordPref = stringPreferencesKey("PASSWORD_PREF")

    private fun getNameFlow(dataStore: DataStore<Preferences>) : Flow<String?> {
        return dataStore.data
            .map { preferences ->
                preferences[namePref]
            }
    }

    private suspend fun getName(dataStore: DataStore<Preferences>) : String? {
        return dataStore.data
            .map { preferences ->
                preferences[namePref]
            }.first()
    }

    private suspend fun getPassword(dataStore: DataStore<Preferences>) : String? {
        return dataStore.data
            .map { preferences ->
                preferences[passwordPref]
            }.first()
    }

    suspend fun isOnboarded(dataStore: DataStore<Preferences>) : Boolean {
        return hasName(dataStore) && hasPassword(dataStore)
    }

    private suspend fun hasName(dataStore: DataStore<Preferences>) : Boolean {
        return !getName(dataStore).isNullOrEmpty()
    }

    private suspend fun hasPassword(dataStore: DataStore<Preferences>) : Boolean {
        return !getPassword(dataStore).isNullOrEmpty()
    }

    private fun isLoggedIn() : Boolean {
        return LoginToken.isLoggedIn
    }

    fun submitName(dataStore: DataStore<Preferences>, name : String?) : Boolean {
        return runBlocking {
            if (name.isNullOrEmpty()) {
                return@runBlocking false
            }
            return@runBlocking try {
                dataStore.edit { prefs ->
                    prefs[namePref] = name
                }
                true
            } catch (ex : Exception) {
                false
            }
        }
    }

    fun submitPassword(dataStore: DataStore<Preferences>, password : String?) : Boolean {
        return runBlocking {
            if (password.isNullOrEmpty()) {
                return@runBlocking false
            }
            return@runBlocking try {
                dataStore.edit { prefs ->
                    prefs[passwordPref] = password
                }
                true
            } catch (ex : Exception) {
                false
            }
        }
    }

    suspend fun start(dataStore : DataStore<Preferences>): NavInstructions {
        return ensureAuthenticatedAndThen(dataStore, ::displayMainModule)
    }

    private fun displayMainModule(): NavInstructions {
        return NavToInstructions(R.id.to_launch)
    }

    private fun displayOnboardingModule(dataStore: DataStore<Preferences>, onboardingFinishedCallback : (Boolean) -> NavInstructions): NavInstructions {
        return OnboardingCoordinator.start(dataStore, onboardingFinishedCallback)
    }

    private fun displayLoginModule(loginFinishedCallback: (Boolean) -> NavInstructions): NavInstructions {
        return LoginCoordinator.start(loginFinishedCallback)
    }

    fun finish() : NavInstructions {
        return NavBackInstruction
    }

    fun abort() : NavInstructions {
        return NavPopInstructions(R.id.main_nav_graph, true)
    }

    private suspend fun ensureAuthenticatedAndThen(
        dataStore : DataStore<Preferences>,
        nextDestination: () -> NavInstructions
    ): NavInstructions {
        return if (!isOnboarded(dataStore)) {
            displayOnboardingModule(dataStore) { onboardingFinished ->
                if (onboardingFinished) {
                    nextDestination()
                } else {
                    abort()
                }
            }
        } else if (!isLoggedIn()) {
            displayLoginModule {
                if (it) {
                    nextDestination()
                } else {
                    abort()
                }
            }
        } else {
            nextDestination()
        }
    }
}