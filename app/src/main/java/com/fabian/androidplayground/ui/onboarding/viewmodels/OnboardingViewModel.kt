package com.fabian.androidplayground.ui.onboarding.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel

class OnboardingViewModel private constructor(dataStore : DataStore<Preferences>) : BaseFragmentViewModel(dataStore) {

    override val TAG = "OnboardingViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            OnboardingViewModel(dataStore) as T
    }
}