package com.fabian.androidplayground.api.finnhub

object CurrencyToSymbolMap {
    private val currencyToSymbolMap = mapOf(
        "USD" to "$"
    )

    private val symbolToCurrencyMap = mapOf(
        "$" to "USD"
    )

    fun getSymbol(currency: String) : String? {
        return currencyToSymbolMap[currency]
    }

    fun getCurrency(symbol: String) : String? {
        return symbolToCurrencyMap[symbol]
    }
}