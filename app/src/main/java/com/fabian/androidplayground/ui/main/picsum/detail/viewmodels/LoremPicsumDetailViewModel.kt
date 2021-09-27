package com.fabian.androidplayground.ui.main.picsum.detail.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fabian.androidplayground.api.picsum.Picsum
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.ui.main.coroutines.viewmodels.CoroutinesViewModel

class LoremPicsumDetailViewModel private constructor(dataStore: DataStore<Preferences>) : BaseFragmentViewModel(dataStore) {
    override val TAG = "LoremPicsumDetailViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            LoremPicsumDetailViewModel(dataStore) as T
    }

    var pic: Picsum? = null
}