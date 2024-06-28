package com.example.currencyconverter.core.balance.local

import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.exchange.rates.model.Currency
import kotlinx.coroutines.flow.Flow

internal interface BalanceLocalDataSource {

    fun getBalances(): Flow<List<CurrencyBalance>>

    suspend fun updateBalance(currency: Currency, amount: Double)

    suspend fun clear()
}
