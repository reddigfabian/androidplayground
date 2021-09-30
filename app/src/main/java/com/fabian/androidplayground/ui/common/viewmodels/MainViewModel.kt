package com.fabian.androidplayground.ui.common.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.datastore.dataStore
import com.fabian.androidplayground.ui.common.coordinators.MainCoordinator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class MainViewModel private constructor(dataStore : DataStore<Preferences>) : BaseFragmentViewModel(dataStore) {

    override val TAG = "MainViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            MainViewModel(dataStore) as T
    }

    private val titleResID = MutableLiveData<Int>()

    init {
        viewModelScope.launch {
            navigationInstructions.emit(MainCoordinator.start(dataStore))
        }
    }

    fun getTitleResID() : LiveData<Int> {
        return titleResID
    }
}