package com.example.currencyconverter.features.exchange.presentation.usecase

import com.example.currencyconverter.core.balance.repository.BalanceRepository
import com.example.currencyconverter.core.exchange.converter.ExchangeEngine
import com.example.currencyconverter.core.exchange.converter.model.ExchangeResult
import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.features.exchange.TestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.math.BigDecimal

internal class ExchangeCurrencyUseCaseTest {

    private val balanceRepository = mockk<BalanceRepository>()
    private val exchangeEngine = mockk<ExchangeEngine>()

    @Test
    fun `converts provided currencies`() = runTest {
        every { balanceRepository.getBalances() } returns flowOf(
            listOf(
                TestData.balance.copy(currency = CURRENCY_EUR, amount = BigDecimal.valueOf(42.0)),
                TestData.balance.copy(currency = CURRENCY_USD, amount = BigDecimal.valueOf(3.0)),
                TestData.balance.copy(currency = CURRENCY_BTC, amount = BigDecimal.valueOf(0.04)),
            )
        )

        coEvery { exchangeEngine.convert(any()) } returns Result.success(
            ExchangeResult(
                tradedAmount = BigDecimal.valueOf(1.0),
                convertedAmount = BigDecimal.valueOf(3.0),
                fee = BigDecimal.valueOf(0.0),
            )
        )

        val result =
            createUseCase().execute(
                base = CURRENCY_EUR,
                rate = EXCHANGE_RATE_BTC,
                amount = BigDecimal.valueOf(3.0)
            )

        assert(result.isSuccess)

        coVerify {
            exchangeEngine.convert(
                ExchangeTransaction(
                    base = CURRENCY_EUR,
                    baseBalance = BigDecimal.valueOf(42.0),
                    amount = BigDecimal.valueOf(3.0),
                    rate = TestData.exchangeRate.copy(
                        currency = CURRENCY_BTC,
                        value = BigDecimal.valueOf(0.000504)
                    ),
                )
            )
        }
    }

    @Test
    fun `updates balances after successful conversion`() = runTest {
        every { balanceRepository.getBalances() } returns flowOf(
            listOf(
                TestData.balance.copy(currency = CURRENCY_EUR, amount = BigDecimal.valueOf(42.0)),
                TestData.balance.copy(currency = CURRENCY_USD, amount = BigDecimal.valueOf(3.0)),
                TestData.balance.copy(currency = CURRENCY_BTC, amount = BigDecimal.valueOf(0.04)),
            )
        )

        val result = ExchangeResult(
            tradedAmount = BigDecimal.valueOf(1.0),
            convertedAmount = BigDecimal.valueOf(3.0),
            fee = BigDecimal.valueOf(0.0),
        )
        coEvery { exchangeEngine.convert(any()) } returns Result.success(result)

        createUseCase().execute(
            base = CURRENCY_EUR,
            rate = EXCHANGE_RATE_BTC,
            amount = BigDecimal.valueOf(3.0)
        )

        coVerify {
            balanceRepository.addToBalance(CURRENCY_BTC, BigDecimal.valueOf(3.0))
            balanceRepository.deductFromBalance(CURRENCY_EUR, BigDecimal.valueOf(1.0))
        }
    }

    private fun createUseCase(): ExchangeCurrencyUseCase {
        return ExchangeCurrencyUseCase(
            balanceRepository = balanceRepository,
            exchangeEngine = exchangeEngine,
        )
    }

    private companion object {
        val CURRENCY_EUR = Currency("EUR")
        val CURRENCY_USD = Currency("USD")
        val CURRENCY_BTC = Currency("BTC")

        val EXCHANGE_RATE_BTC =
            TestData.exchangeRate.copy(
                currency = CURRENCY_BTC,
                value = BigDecimal.valueOf(0.000504)
            )
    }
}
