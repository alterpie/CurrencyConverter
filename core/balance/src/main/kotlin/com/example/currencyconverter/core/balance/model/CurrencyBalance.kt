package com.example.currencyconverter.core.balance.model

import com.example.currencyconverter.core.currency.model.Currency

data class CurrencyBalance(
    val currency: Currency,
    val amount: Double,
)