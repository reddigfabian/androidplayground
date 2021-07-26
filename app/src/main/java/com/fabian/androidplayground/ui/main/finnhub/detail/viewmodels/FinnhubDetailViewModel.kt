package com.fabian.androidplayground.ui.main.finnhub.detail.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fabian.androidplayground.api.finnhub.FinnhubStockSymbol
import com.fabian.androidplayground.ui.main.finnhub.list.viewmodels.AbstractFinnhubViewModel

class FinnhubDetailViewModel private constructor(finnhubStock: FinnhubStockSymbol) : AbstractFinnhubViewModel(finnhubStock) {
    class Factory(private val finnhubStock: FinnhubStockSymbol) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            FinnhubDetailViewModel(finnhubStock) as T
    }
}