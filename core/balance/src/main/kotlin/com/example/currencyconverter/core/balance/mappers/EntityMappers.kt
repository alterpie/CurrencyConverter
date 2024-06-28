package com.example.currencyconverter.core.balance.mappers

import com.example.currencyconverter.core.balance.local.database.model.CurrencyBalanceEntity
import com.example.currencyconverter.core.balance.model.CurrencyBalance

internal fun CurrencyBalanceEntity.map(): CurrencyBalance {
    return CurrencyBalance(currency, amount)
}
