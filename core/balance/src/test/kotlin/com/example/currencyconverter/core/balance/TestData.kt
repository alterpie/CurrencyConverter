package com.example.currencyconverter.core.balance

import com.example.currencyconverter.core.balance.local.database.model.CurrencyBalanceEntity
import com.example.currencyconverter.core.exchange.rates.model.Currency
import java.math.BigDecimal

internal object TestData {

    val currencyBalanceEntity: CurrencyBalanceEntity
        get() = CurrencyBalanceEntity(
            currency = Currency("EUR"),
            amount = BigDecimal.valueOf(42.0),
        )
}
