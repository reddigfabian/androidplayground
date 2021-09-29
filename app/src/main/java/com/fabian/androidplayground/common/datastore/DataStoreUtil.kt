package com.fabian.androidplayground.common.datastore

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import com.fabian.androidplayground.common.auth.LoginToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object DataStoreUtil {
    private val NAME_PREF = stringPreferencesKey("NAME_PREF")
    private val PASSWORD_PREF = stringPreferencesKey("PASSWORD_PREF")

    fun getNameFlow(context : Context) : Flow<String?> {
        return context.dataStore.data
            .map { preferences ->
                preferences[NAME_PREF]
            }
    }

    suspend fun getName(context : Context) : String? {
        return context.dataStore.data
            .map { preferences ->
                preferences[NAME_PREF]
            }.first()
    }

    suspend fun getPassword(context : Context) : String? {
        return context.dataStore.data
            .map { preferences ->
                preferences[PASSWORD_PREF]
            }.first()
    }

    suspend fun isOnboarded(context: Context) : Boolean {
        return hasName(context) || hasPassword(context)
    }

    suspend fun hasName(context: Context) : Boolean {
        return !getName(context).isNullOrEmpty()
    }

    suspend fun hasPassword(context: Context) : Boolean {
        return !getPassword(context).isNullOrEmpty()
    }

    fun isLoggedIn() : Boolean {
        return LoginToken.isLoggedIn
    }
}