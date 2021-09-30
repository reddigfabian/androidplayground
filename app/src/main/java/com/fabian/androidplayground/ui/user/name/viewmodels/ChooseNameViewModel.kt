package com.fabian.androidplayground.ui.user.name.viewmodels

import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.ui.user.login.coordinators.LoginCoordinator
import com.fabian.androidplayground.ui.user.name.coordinators.NameCoordinator
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ChooseNameViewModel private constructor(dataStore : DataStore<Preferences>) : BaseFragmentViewModel(dataStore) {

    override val TAG = "ChooseNameViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ChooseNameViewModel(dataStore) as T
    }

    val name = MutableLiveData<String?>(null)

    fun submitClick(v : View) {
        name.value?.let { nonNullName ->
            viewModelScope.launch {
                navigationInstructions.emit(NameCoordinator.finish(nonNullName))
            }
        }
    }

    fun submitNameAsync(name : String?) = viewModelScope.async {
        if (name.isNullOrEmpty()) {
            return@async false
        }
        try {
            dataStore.edit { prefs ->
                prefs[namePref] = name
            }
            true
        } catch (ex : Exception) {
            false
        }
    }

    override fun onBackPressed() {
        viewModelScope.launch {
            navigationInstructions.emit(NameCoordinator.abort())
        }
    }
}