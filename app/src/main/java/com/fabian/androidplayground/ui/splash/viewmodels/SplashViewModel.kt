package com.fabian.androidplayground.ui.splash.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel

class SplashViewModel private constructor(dataStore : DataStore<Preferences>) : BaseFragmentViewModel(dataStore) {

    override val TAG = "SplashViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            SplashViewModel(dataStore) as T
    }
}