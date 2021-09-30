package com.fabian.androidplayground.ui.user.login.viewmodels

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.fabian.androidplayground.R
import com.fabian.androidplayground.common.auth.LoginToken
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.common.navigation.NavPopInstructions
import com.fabian.androidplayground.ui.user.login.coordinators.LoginCoordinator
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch

class LoginViewModel private constructor(dataStore : DataStore<Preferences>) : BaseFragmentViewModel(dataStore) {

    override val TAG = "LoginViewModel"

    class Factory(private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            LoginViewModel(dataStore) as T
    }

    val name = MutableLiveData<String?>(null)
    val password = MutableLiveData<String?>(null)

    fun submitClick(v : View) {
        viewModelScope.launch {
            if (verifyCredentials()) {
                LoginToken.isLoggedIn = true
                navigationInstructions.emit(LoginCoordinator.finish())
            } else {
                LoginToken.isLoggedIn = false
                Toast.makeText(v.context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun verifyCredentials() : Boolean {
        return name.value?.let { nonNullName ->
            password.value?.let { nonNullPassword ->
                verifyName(nonNullName) && verifyPassword(nonNullPassword)
            } ?: kotlin.run {
                false
            }
        } ?: kotlin.run {
            false
        }
    }

    private suspend fun verifyName(name : String) : Boolean {
        Log.d(TAG, "verifyName: $password")
        val match = getName() == name
        LoginToken.isLoggedIn = match
        Log.d(TAG, "verifyName: verified = $match")
        return match
    }

    private suspend fun verifyPassword(password : String) : Boolean {
        Log.d(TAG, "verifyPassword: $password")
        val match = getPassword() == password
        LoginToken.isLoggedIn = match
        Log.d(TAG, "verifyPassword: verified = $match")
        return match
    }

    override fun onBackPressed() {
        viewModelScope.launch {
            navigationInstructions.emit(LoginCoordinator.abort())
        }
    }
}