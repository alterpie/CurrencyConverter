package com.example.currencyconverter.core.balance.model

import com.example.currencyconverter.core.exchange.rates.model.Currency
import java.math.BigDecimal

data class CurrencyBalance(
    val currency: Currency,
    val amount: BigDecimal,
)