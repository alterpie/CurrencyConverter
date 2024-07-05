package com.example.currencyconverter.core.exchange.converter.model

import java.math.BigDecimal

data class ExchangeResult(
    val tradedAmount: BigDecimal,
    val convertedAmount: BigDecimal,
    val fee: BigDecimal,
)
