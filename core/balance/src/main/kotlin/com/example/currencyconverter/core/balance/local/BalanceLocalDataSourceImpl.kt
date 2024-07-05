package com.example.currencyconverter.core.balance.local

import com.example.currencyconverter.core.balance.local.database.dao.CurrencyBalanceDao
import com.example.currencyconverter.core.balance.local.database.model.CurrencyBalanceEntity
import com.example.currencyconverter.core.balance.mappers.map
import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.exchange.rates.model.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

internal class BalanceLocalDataSourceImpl @Inject constructor(
    private val currencyBalanceDao: CurrencyBalanceDao,
) : BalanceLocalDataSource {

    override fun getBalances(): Flow<List<CurrencyBalance>> {
        return currencyBalanceDao.getBalances()
            .map { balances -> balances.map { it.map() } }
    }

    override suspend fun addToBalance(currency: Currency, amount: BigDecimal) {
        val storedBalance = currencyBalanceDao.getBalance(currency)
        val updatedBalance = storedBalance?.let {
            it.copy(amount = it.amount + amount)
        } ?: CurrencyBalanceEntity(currency, amount.setScale(6, RoundingMode.HALF_DOWN))
        currencyBalanceDao.updateBalance(updatedBalance)
    }

    override suspend fun deductFromBalance(currency: Currency, amount: BigDecimal) {
        currencyBalanceDao.getBalance(currency)?.let {
            currencyBalanceDao.updateBalance(it.copy(amount = it.amount - amount))
        }
    }

    override suspend fun clear() {
        currencyBalanceDao.deleteAll()
    }
}
