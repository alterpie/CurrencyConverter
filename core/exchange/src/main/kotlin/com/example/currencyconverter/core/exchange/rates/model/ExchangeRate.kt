package com.example.currencyconverter.core.exchange.rates.model

import java.math.BigDecimal

data class ExchangeRate(
    val currency: Currency,
    val value: BigDecimal,
)
