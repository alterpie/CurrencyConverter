package com.example.currencyconverter.core.exchange.converter.model

import com.example.currencyconverter.core.exchange.rates.model.Currency

data class ExchangeResult(
    val tradedAmount: Double,
    val convertedAmount: Double,
    val fee: Double,
    val feeCurrency: Currency,
)
