package com.example.currencyconverter.core.balance.repository

import com.example.currencyconverter.core.balance.local.BalanceLocalDataSource
import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.exchange.rates.model.Currency
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import javax.inject.Inject

internal class BalanceRepositoryImpl @Inject constructor(
    private val balanceLocalDataSource: BalanceLocalDataSource
) : BalanceRepository {

    override fun getBalances(): Flow<List<CurrencyBalance>> {
        return balanceLocalDataSource.getBalances()
    }

    override suspend fun addToBalance(currency: Currency, amount: BigDecimal) {
        balanceLocalDataSource.addToBalance(currency, amount)
    }

    override suspend fun deductFromBalance(currency: Currency, amount: BigDecimal) {
        balanceLocalDataSource.deductFromBalance(currency, amount)
    }

    override suspend fun clearAll() {
        balanceLocalDataSource.clear()
    }
}