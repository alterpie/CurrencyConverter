package com.example.currencyconverter.core.balance

import com.example.currencyconverter.core.balance.local.database.model.CurrencyBalanceEntity
import com.example.currencyconverter.core.exchange.rates.model.Currency

internal object TestData {

    val currencyBalanceEntity: CurrencyBalanceEntity
        get() = CurrencyBalanceEntity(
            currency = Currency("EUR"),
            amount = 42.0,
        )
}
