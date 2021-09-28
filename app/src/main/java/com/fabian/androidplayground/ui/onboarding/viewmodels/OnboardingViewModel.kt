package com.fabian.androidplayground.ui.onboarding.viewmodels

import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.auth.LoginToken
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.navigation.IntentNavArgs
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions
import kotlinx.coroutines.async

class OnboardingViewModel private constructor(dataStore : DataStore<Preferences>,
                                              nextID : Int,
                                              nextIntentArgs : IntentNavArgs?) : BaseFragmentViewModel(dataStore, nextID, nextIntentArgs) {

    override val TAG = "OnboardingViewModel"

    class Factory(private val dataStore : DataStore<Preferences>,
                  private val nextID : Int,
                  private val nextIntentArgs : IntentNavArgs?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            OnboardingViewModel(dataStore, nextID, nextIntentArgs) as T
    }

    val onboardingSuccess = MutableLiveData(false)

    override fun startUpCheck() = viewModelScope.async {
        Log.d(TAG, "startUpCheck: onboarding")
        if (!hasName().await()) {
            navigationInstructions.emit(NavToInstructions(R.id.to_choose_name))
            return@async true
        } else if (!hasPassword().await()) {
                navigationInstructions.emit(NavToInstructions(R.id.to_choose_password))
            return@async true
        } else {
            LoginToken.isLoggedIn = true
            onboardingSuccess.postValue(true)
            if (nextID != 0) {
                val b = Bundle()
                b.putParcelable(IntentNavArgs.PARCEL_KEY, nextIntentArgs)
                navigationInstructions.emit(NavToInstructions(nextID, b))
            } else {
                navigationInstructions.emit(NavPopInstructions(R.id.onboarding_nav_graph, true))
            }
            return@async true
        }
    }
}