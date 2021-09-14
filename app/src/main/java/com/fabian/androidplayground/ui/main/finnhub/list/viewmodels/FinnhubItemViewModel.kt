package com.fabian.androidplayground.ui.main.finnhub.list.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import com.fabian.androidplayground.BR
import com.fabian.androidplayground.R
import com.fabian.androidplayground.api.finnhub.CurrencyToSymbolMap
import com.fabian.androidplayground.api.finnhub.FinnhubApi
import com.fabian.androidplayground.api.finnhub.FinnhubStockSymbol
import com.fabian.androidplayground.api.finnhub.Subscribe
import com.fabian.androidplayground.common.recyclerview.viewmodels.RecyclerItem
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "FinnhubItemViewModel"

@SuppressLint("CheckResult")
class FinnhubItemViewModel(finnhubStock: FinnhubStockSymbol) : AbstractFinnhubViewModel(finnhubStock) {
    private val currencySymbol = CurrencyToSymbolMap.getSymbol(finnhubStock.currency)
    val price = MutableLiveData("$currencySymbol${finnhubStock.quote?.c}")
    var job : Job? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun observeTicker() {
        job?.cancel()
        job = viewModelScope.launch(IO) {
            FinnhubApi.finnhubApiScarletService.observeTicker().collect {
                val tickerData = it.data.filter { data ->
                    data.s == finnhubStock.symbol
                }
                if (tickerData.isNotEmpty()) {
                    Log.d(TAG, "Got a Ticker: $it")
                    price.postValue("$currencySymbol${tickerData[0].p}")
                }
            }
        }
        FinnhubApi.finnhubApiScarletService.sendSubscribe(Subscribe(finnhubStock.symbol))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopObserveTicker() {
        job?.cancel()
        job = null
        FinnhubApi.finnhubApiScarletService.sendSubscribe(Subscribe(finnhubStock.symbol))
    }
}

fun FinnhubItemViewModel.toRecyclerItem() =
    RecyclerItem(
        data = this,
        variableId = BR.finnhubItemViewModel,
        layoutID = R.layout.item_finnhub_list
    )