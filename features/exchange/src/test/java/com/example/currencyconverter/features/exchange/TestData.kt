package com.example.currencyconverter.features.exchange

import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import java.math.BigDecimal

internal object TestData {

    val balance: CurrencyBalance
        get() = CurrencyBalance(
            currency = Currency("EUR"),
            amount = BigDecimal.valueOf(42.0),
        )

    val exchangeRate: ExchangeRate
        get() = ExchangeRate(
            currency = Currency("USD"),
            value = BigDecimal.valueOf(1.21),
        )
}