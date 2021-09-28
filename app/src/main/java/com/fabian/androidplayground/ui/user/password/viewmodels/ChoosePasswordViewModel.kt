package com.fabian.androidplayground.ui.user.password.viewmodels

import android.os.Bundle
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
import com.fabian.androidplayground.common.navigation.IntentNavArgs
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ChoosePasswordViewModel private constructor(dataStore : DataStore<Preferences>,
                                                  nextID : Int,
                                                  nextIntentArgs : IntentNavArgs?) : BaseFragmentViewModel(dataStore, nextID, nextIntentArgs) {

    override val TAG = "ChoosePasswordViewModel"

    class Factory(private val dataStore : DataStore<Preferences>,
                  private val nextID : Int,
                  private val nextIntentArgs : IntentNavArgs?) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ChoosePasswordViewModel(dataStore, nextID, nextIntentArgs) as T
    }

    val password = MutableLiveData<String?>(null)

    fun submitClick(v : View) {
        password.value?.let { nonNullPassword ->
            viewModelScope.launch {
                if (submitPasswordAsync(nonNullPassword).await()) {
                    if (nextID != 0) {
                        val b = Bundle()
                        b.putParcelable(IntentNavArgs.PARCEL_KEY, nextIntentArgs)
                        navigationInstructions.emit(NavToInstructions(nextID, b))
                    } else {
                        navigationInstructions.emit(NavPopInstructions(R.id.choose_password_nav_graph, true))
                    }
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