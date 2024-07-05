package com.example.currencyconverter.core.exchange.converter.model

import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import java.math.BigDecimal

data class ExchangeTransaction(
    val base: Currency,
    val baseBalance: BigDecimal,
    val amount: BigDecimal,
    val rate: ExchangeRate,
)
