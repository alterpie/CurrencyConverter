package com.example.currencyconverter.core.exchange.converter

import com.example.currencyconverter.core.exchange.converter.error.ExchangeError
import com.example.currencyconverter.core.exchange.converter.model.ExchangeResult
import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import com.example.currencyconverter.core.exchange.fees.FeeResolver
import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class ExchangeEngineImplTest {

    private val feeResolver = mockk<FeeResolver>()

    @Test
    fun `returns error when not enough balance for exchange`() = runTest {
        val result = createEngine().convert(
            ExchangeTransaction(
                baseBalance = 42.0,
                amount = 50.0,
                base = BASE,
                rate = ExchangeRate(TARGET, 1.21)
            )
        )

        assert(result.isFailure)
        assert(result.exceptionOrNull() is ExchangeError.NotEnoughBalance)
    }

    @Test
    fun `returns error when conversion fee exceeds balance`() = runTest {
        coEvery { feeResolver.resolve(any()) } returns 100.0
        val result = createEngine().convert(
            ExchangeTransaction(
                baseBalance = 42.0,
                amount = 5.0,
                base = BASE,
                rate = ExchangeRate(TARGET, 1.21)
            )
        )

        assert(result.isFailure)
        assert(result.exceptionOrNull() is ExchangeError.FeeTooHigh)
    }

    @Test
    fun `converts provided amount`() = runTest {
        coEvery { feeResolver.resolve(any()) } returns 3.0
        val result = createEngine().convert(
            ExchangeTransaction(
                baseBalance = 42.0,
                amount = 5.0,
                base = BASE,
                rate = ExchangeRate(TARGET, 1.21)
            )
        )

        assert(result.isSuccess)
        val expected = ExchangeResult(
            tradedAmount = 5.0,
            convertedAmount = 5.0 * 1.21,
            fee = 3.0
        )
        assert(result.getOrNull() == expected)
    }

    private fun createEngine(): ExchangeEngineImpl {
        return ExchangeEngineImpl(feeResolver)
    }

    private companion object {
        val BASE = Currency("EUR")
        val TARGET = Currency("USD")
    }
}