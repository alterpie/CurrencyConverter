package com.example.currencyconverter.features.exchange

import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate

internal object TestData {

    val balance: CurrencyBalance
        get() = CurrencyBalance(
            currency = Currency("EUR"),
            amount = 42.0,
        )

    val exchangeRate: ExchangeRate
        get() = ExchangeRate(
            currency = Currency("USD"),
            value = 1.21,
        )
}