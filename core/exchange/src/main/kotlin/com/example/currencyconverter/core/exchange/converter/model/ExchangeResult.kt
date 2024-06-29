package com.example.currencyconverter.core.exchange.converter.model

data class ExchangeResult(
    val oldBaseBalance: Double,
    val newBaseBalance: Double,
    val convertedAmount: Double,
    val fee: Double,
)
