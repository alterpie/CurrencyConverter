package com.example.currencyconverter.core.balance.repository

import com.example.currencyconverter.core.balance.local.BalanceLocalDataSource
import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.exchange.rates.model.Currency
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class BalanceRepositoryImpl @Inject constructor(
    private val balanceLocalDataSource: BalanceLocalDataSource
) : BalanceRepository {

    override fun getBalances(): Flow<List<CurrencyBalance>> {
        return balanceLocalDataSource.getBalances()
    }

    override suspend fun updateBalance(currency: Currency, amount: Double) {
        balanceLocalDataSource.updateBalance(currency, amount)
    }

    override suspend fun clearAll() {
        balanceLocalDataSource.clear()
    }
}