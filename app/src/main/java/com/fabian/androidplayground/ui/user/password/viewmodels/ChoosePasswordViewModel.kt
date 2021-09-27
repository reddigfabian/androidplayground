package com.fabian.androidplayground.ui.user.password.viewmodels

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

class ChoosePasswordViewModel private constructor(dataStore : DataStore<Preferences>) : BaseFragmentViewModel(dataStore) {

    override val TAG = "ChoosePasswordViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ChoosePasswordViewModel(dataStore) as T
    }

    val password = MutableLiveData<String?>(null)

    fun submitClick(v : View) {
        password.value?.let { nonNullPassword ->
            viewModelScope.launch {
                if (submitPasswordAsync(nonNullPassword).await()) {
                    navigationInstructions.emit(NavPopInstructions(R.id.choose_password_nav_graph, true))
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

    private fun submitPasswordAsync(password : String) = viewModelScope.async {
        try {
            dataStore.edit { prefs ->
                prefs[PASSWORD_PREF] = password
            }
            true
        } catch (ex : Exception) {
            false
        }
    }
}