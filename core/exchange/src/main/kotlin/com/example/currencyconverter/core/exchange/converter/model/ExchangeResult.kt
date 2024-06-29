package com.example.currencyconverter.core.exchange.converter.model

import com.example.currencyconverter.core.exchange.rates.model.Currency

data class ExchangeResult(
    val oldBaseBalance: Double,
    val newBaseBalance: Double,
    val baseCurrency: Currency,
    val convertedAmount: Double,
    val convertedCurrency: Currency,
    val fee: Double,
    val feeCurrency: Currency,
)
