package com.fabian.androidplayground.common.databinding

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.*
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.auth.LoginToken
import com.fabian.androidplayground.common.navigation.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

abstract class BaseFragmentViewModel(
    protected val dataStore : DataStore<Preferences>,
    protected val nextID : Int = 0,
    protected val nextIntentArgs : IntentNavArgs? = null
) : ViewModel(), LifecycleObserver {

    abstract val TAG : String

    protected val NAME_PREF = stringPreferencesKey("NAME_PREF")
    protected val PASSWORD_PREF = stringPreferencesKey("PASSWORD_PREF")
    protected fun getNameFlow() : Flow<String?> {
        return dataStore.data
            .map { preferences ->
                preferences[NAME_PREF]
            }
    }
    protected suspend fun getName() : String? {
        return dataStore.data
            .map { preferences ->
                preferences[NAME_PREF]
            }.first()
    }
    protected suspend fun getPassword() : String? {
        return dataStore.data
            .map { preferences ->
                preferences[PASSWORD_PREF]
            }.first()
    }

    val navigationInstructions = MutableSharedFlow<NavInstructions>()

    @Suppress("DeferredResultUnused")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun doOnStart() {
        Log.d(TAG, "doOnStart")
        startUpCheck()
    }

    open fun startUpCheck() : Deferred<Boolean>? = viewModelScope.async {
        Log.d(TAG, "startUpCheck: base")
        if (!isOnboarded()) {
            Log.d(TAG, "startUpCheck: not onboarded")
            navigationInstructions.emit(NavToInstructions(R.id.to_onboarding))
            true
        } else if (!isLoggedIn()) {
            Log.d(TAG, "startUpCheck: not logged in")
            navigationInstructions.emit(NavToInstructions(R.id.to_login))
            true
        } else {
            Log.d(TAG, "startUpCheck: all good")
            false
        }
    }

    private suspend fun isOnboarded() : Boolean {
        Log.d(TAG, "startUpCheck: checking onboarding")
        return !listOf(hasName(), hasPassword()).awaitAll().contains(false)
    }

    protected fun hasName() = viewModelScope.async {
        !getName().isNullOrEmpty()
    }

    protected suspend fun hasPassword() = viewModelScope.async {
        !getPassword().isNullOrEmpty()
    }

    private fun isLoggedIn() : Boolean {
        Log.d(TAG, "startUpCheck: checking logged in")
        return LoginToken.isLoggedIn
    }
}