package com.example.currencyconverter.core.balance.repository

import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.exchange.rates.model.Currency
import kotlinx.coroutines.flow.Flow

interface BalanceRepository {

    fun getBalances(): Flow<List<CurrencyBalance>>

    suspend fun addToBalance(currency: Currency, amount: Double)

    suspend fun deductFromBalance(currency: Currency, amount: Double)

    suspend fun clearAll()
}