package com.fabian.androidplayground.ui.main.finnhub.detail.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fabian.androidplayground.api.finnhub.FinnhubStockSymbol
import com.fabian.androidplayground.common.databinding.BaseFragmentViewModel
import com.fabian.androidplayground.ui.main.coroutines.viewmodels.CoroutinesViewModel

class FinnhubDetailViewModel private constructor(val finnhubStock: FinnhubStockSymbol, dataStore: DataStore<Preferences>) : BaseFragmentViewModel(dataStore) {
    override val TAG = "FinnhubDetailViewModel"

    class Factory(private val finnhubStock: FinnhubStockSymbol, private val dataStore : DataStore<Preferences>) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            FinnhubDetailViewModel(finnhubStock, dataStore) as T
    }
}