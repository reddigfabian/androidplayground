package com.fabian.androidplayground.ui.onboarding.viewmodels

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.auth.LoginToken
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions
import kotlinx.coroutines.async

class OnboardingViewModel private constructor(dataStore : DataStore<Preferences>) : BaseFragmentViewModel(dataStore) {

    override val TAG = "OnboardingViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            OnboardingViewModel(dataStore) as T
    }

    override fun startUpCheck() = viewModelScope.async {
        Log.d(TAG, "startUpCheck: onboarding")
        if (!hasName().await()) {
            if (cancel) {
                cancel = false
                navigationInstructions.emit(NavToInstructions(R.id.to_choose_name))
            } else {
                cancel = true
                navigationInstructions.emit(NavPopInstructions(R.id.onboarding_nav_graph, true))
            }
            return@async true
        } else if (!hasPassword().await()) {
            if (cancel) {
                cancel = false
                navigationInstructions.emit(NavToInstructions(R.id.to_choose_password))
            } else {
                cancel = true
                navigationInstructions.emit(NavPopInstructions(R.id.onboarding_nav_graph, true))
            }
            return@async true
        } else {
            cancel = true
            LoginToken.isLoggedIn = true
            navigationInstructions.emit(NavPopInstructions(R.id.onboarding_nav_graph, true))
            return@async true
        }
    }
}