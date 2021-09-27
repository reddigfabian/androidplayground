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
                if (submitNameAsync(nonNullName).await()) {
                    navigationInstructions.emit(NavPopInstructions(R.id.choose_name_nav_graph, true))
                } else {
                    Toast.makeText(v.context, "Something went wrong :(", Toast.LENGTH_SHORT).show()
                    navigationInstructions.emit(NavBackInstruction)
                }
            }
        }
    }

    override fun startUpCheck(): Deferred<Boolean>? {
        return null
    }

    private fun submitNameAsync(name : String) = viewModelScope.async {
        try {
            dataStore.edit { prefs ->
                prefs[NAME_PREF] = name
            }
            true
        } catch (ex : Exception) {
            false
        }
    }

}