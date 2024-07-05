package com.example.currencyconverter.core.balance.local

import com.example.currencyconverter.core.balance.TestData
import com.example.currencyconverter.core.balance.local.database.dao.CurrencyBalanceDao
import com.example.currencyconverter.core.balance.local.database.model.CurrencyBalanceEntity
import com.example.currencyconverter.core.exchange.rates.model.Currency
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

internal class BalanceLocalDataSourceImplTest {

    private val currencyBalanceDao = mockk<CurrencyBalanceDao>()

    @Test
    fun `updates existing balance`() = runTest {
        val storedBalance = TestData.currencyBalanceEntity.copy(currency = CURRENCY_EUR)
        coEvery { currencyBalanceDao.getBalance(any()) } returns storedBalance

        createDataSource().addToBalance(CURRENCY_EUR, BigDecimal.valueOf(12.0))

        coVerify {
            currencyBalanceDao.updateBalance(
                storedBalance.copy(
                    amount = storedBalance.amount + BigDecimal.valueOf(
                        12.0
                    )
                )
            )
        }
    }

    @Test
    fun `adds new balance`() = runTest {
        coEvery { currencyBalanceDao.getBalance(any()) } returns null

        createDataSource().addToBalance(CURRENCY_EUR, BigDecimal.valueOf(12.0))

        coVerify {
            currencyBalanceDao.updateBalance(
                CurrencyBalanceEntity(
                    CURRENCY_EUR,
                    BigDecimal.valueOf(12.00).setScale(6, RoundingMode.HALF_DOWN)
                )
            )
        }
    }

    @Test
    fun `deducts from existing balance`() = runTest {
        val storedBalance = TestData.currencyBalanceEntity
        coEvery { currencyBalanceDao.getBalance(any()) } returns storedBalance

        createDataSource().deductFromBalance(CURRENCY_EUR, BigDecimal.valueOf(4.0))

        coVerify {
            currencyBalanceDao.updateBalance(
                storedBalance.copy(
                    amount = storedBalance.amount - BigDecimal.valueOf(
                        4.0
                    )
                )
            )
        }
    }

    @Test
    fun `no deduction when balance does not exist`() = runTest {
        coEvery { currencyBalanceDao.getBalance(any()) } returns null

        createDataSource().deductFromBalance(CURRENCY_EUR, BigDecimal.valueOf(4.0))

        coVerify {
            currencyBalanceDao.getBalance(CURRENCY_EUR)
        }

        confirmVerified(currencyBalanceDao)
    }

    private fun createDataSource(): BalanceLocalDataSourceImpl {
        return BalanceLocalDataSourceImpl(currencyBalanceDao)
    }

    private companion object {
        val CURRENCY_EUR = Currency("EUR")
    }
}
