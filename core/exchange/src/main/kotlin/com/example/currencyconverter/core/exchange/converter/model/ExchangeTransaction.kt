package com.example.currencyconverter.core.exchange.converter.model

import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate

data class ExchangeTransaction(
    val base: Currency,
    val baseBalance: Double,
    val amount: Double,
    val rate: ExchangeRate,
)
