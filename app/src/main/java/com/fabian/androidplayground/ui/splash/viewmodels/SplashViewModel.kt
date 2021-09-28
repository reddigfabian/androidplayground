package com.fabian.androidplayground.ui.splash.viewmodels

import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.navigation.IntentNavArgs
import com.fabian.androidplayground.common.navigation.NavBackInstruction
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.common.navigation.NavToInstructions
import kotlinx.coroutines.async

class SplashViewModel private constructor(private val nextID : Int,
                                          private val nextIntentArgs : IntentNavArgs?,
                                          dataStore : DataStore<Preferences>) : BaseFragmentViewModel(dataStore) {

    override val TAG = "SplashViewModel"

    class Factory(private val nextID : Int,
                  private val nextIntentArgs : IntentNavArgs?,
                  private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            SplashViewModel(nextID, nextIntentArgs, dataStore) as T
    }

    override fun startUpCheck() = viewModelScope.async {
        Log.d(TAG, "startUpCheck: splash")
        val await = super.startUpCheck()?.await() ?: false
        Log.d(TAG, "startUpCheck: came back from super. ${if (await) "super handled startup" else "super didn't handle startup"}")
        if (!await) {
            val b = Bundle()
            b.putParcelable("intentArgs", nextIntentArgs)
            navigationInstructions.emit(NavToInstructions(nextID, b))
            return@async true
        }
        return@async false
    }
}