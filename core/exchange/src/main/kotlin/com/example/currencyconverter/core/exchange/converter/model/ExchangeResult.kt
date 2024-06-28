package com.example.currencyconverter.core.exchange.converter.model

data class ExchangeResult(
    val newBaseBalance: Double,
    val convertedAmount: Double,
    val fee: Double,
)
