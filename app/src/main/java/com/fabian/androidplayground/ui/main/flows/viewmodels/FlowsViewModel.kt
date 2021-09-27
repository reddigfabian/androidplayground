package com.fabian.androidplayground.ui.main.flows.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel

class FlowsViewModel private constructor(dataStore: DataStore<Preferences>): BaseFragmentViewModel(dataStore) {
    override val TAG = "FlowsViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            FlowsViewModel(dataStore) as T
    }
}