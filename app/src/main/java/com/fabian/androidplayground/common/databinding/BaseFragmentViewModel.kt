package com.fabian.androidplayground.common.databinding

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.*
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.auth.LoginToken
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.common.navigation.NavInstructions
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

abstract class BaseFragmentViewModel(protected val dataStore : DataStore<Preferences>) : ViewModel(), LifecycleObserver {

    abstract val TAG : String

    protected val namePref = stringPreferencesKey("NAME_PREF")
    protected val passwordPref = stringPreferencesKey("PASSWORD_PREF")
    protected fun getNameFlow() : Flow<String?> {
        return dataStore.data
            .map { preferences ->
                preferences[namePref]
            }
    }
    protected suspend fun getName() : String? {
        return dataStore.data
            .map { preferences ->
                preferences[namePref]
            }.first()
    }
    protected suspend fun getPassword() : String? {
        return dataStore.data
            .map { preferences ->
                preferences[passwordPref]
            }.first()
    }

    val navigationInstructions = MutableSharedFlow<NavInstructions>()

    suspend fun isOnboarded() : Boolean {
        Log.d(TAG, "startUpCheck: checking onboarding")
        return !listOf(hasName(), hasPassword()).awaitAll().contains(false)
    }

    private fun hasName() = viewModelScope.async {
        !getName().isNullOrEmpty()
    }

    private fun hasPassword() = viewModelScope.async {
        !getPassword().isNullOrEmpty()
    }

    fun isLoggedIn() : Boolean {
        Log.d(TAG, "startUpCheck: checking logged in")
        return LoginToken.isLoggedIn
    }
}