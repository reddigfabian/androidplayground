package com.fabian.androidplayground.ui.main.finnhub.list.viewmodels

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fabian.androidplayground.api.finnhub.FinnhubStockSymbol

abstract class AbstractFinnhubViewModel(val finnhubStock: FinnhubStockSymbol) : ViewModel(),
    LifecycleObserver {
    val stockName = MutableLiveData("${finnhubStock.description} (${finnhubStock.symbol})")
}