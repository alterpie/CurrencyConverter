package com.example.currencyconverter.core.balance.local

import com.example.currencyconverter.core.balance.local.database.dao.CurrencyBalanceDao
import com.example.currencyconverter.core.balance.local.database.model.CurrencyBalanceEntity
import com.example.currencyconverter.core.balance.mappers.map
import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.currency.model.Currency
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

    override suspend fun updateBalance(currency: Currency, amount: Double) {
        currencyBalanceDao.updateBalance(CurrencyBalanceEntity(currency, amount))
    }

    override suspend fun clear() {
        currencyBalanceDao.deleteAll()
    }
}
