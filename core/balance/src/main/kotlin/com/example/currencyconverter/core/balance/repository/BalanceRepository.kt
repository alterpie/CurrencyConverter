package com.example.currencyconverter.core.balance.repository

import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.currency.model.Currency
import kotlinx.coroutines.flow.Flow

interface BalanceRepository {

    fun getBalances(): Flow<List<CurrencyBalance>>

    suspend fun updateBalance(currency: Currency, amount: Double)

    suspend fun clearAll()
}