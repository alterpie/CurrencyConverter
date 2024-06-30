package com.example.currencyconverter.core.balance.local

import com.example.currencyconverter.core.balance.local.database.dao.CurrencyBalanceDao
import com.example.currencyconverter.core.balance.local.database.model.CurrencyBalanceEntity
import com.example.currencyconverter.core.balance.mappers.map
import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.exchange.rates.model.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class BalanceLocalDataSourceImpl @Inject constructor(
    private val currencyBalanceDao: CurrencyBalanceDao,
) : BalanceLocalDataSource {

    override fun getBalances(): Flow<List<CurrencyBalance>> {
        return currencyBalanceDao.getBalances()
            .map { balances -> balances.map { it.map() } }
    }

    override suspend fun addToBalance(currency: Currency, amount: Double) {
        val storedBalance = currencyBalanceDao.getBalance(currency)
        val updatedBalance = storedBalance?.let {
            it.copy(amount = it.amount + amount)
        } ?: CurrencyBalanceEntity(currency, amount)
        currencyBalanceDao.updateBalance(updatedBalance)
    }

    override suspend fun deductFromBalance(currency: Currency, amount: Double) {
        currencyBalanceDao.getBalance(currency)?.let {
            currencyBalanceDao.updateBalance(it.copy(amount = it.amount - amount))
        }
    }

    override suspend fun clear() {
        currencyBalanceDao.deleteAll()
    }
}
