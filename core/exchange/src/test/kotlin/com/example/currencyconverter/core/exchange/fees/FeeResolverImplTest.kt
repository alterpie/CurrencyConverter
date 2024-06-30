package com.example.currencyconverter.core.exchange.fees

import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class FeeResolverImplTest {

    @Test
    fun `substitute applier cancels all fees`() = runTest {
        val accumulateApplier = object : FeeApplier {
            override suspend fun apply(exchangeTransaction: ExchangeTransaction): FeeWeight {
                return FeeWeight.Accumulative(exchangeTransaction.baseBalance * 0.01)
            }
        }
        val substituteApplier = object : FeeApplier {
            override suspend fun apply(exchangeTransaction: ExchangeTransaction): FeeWeight {
                return FeeWeight.SubstituteAll(42.0)
            }
        }

        val fee = createResolver(setOf(accumulateApplier, substituteApplier)).resolve(
            ExchangeTransaction(
                base = BASE,
                rate = ExchangeRate(TARGET, 1.21),
                baseBalance = 100.0,
                amount = 3.0,
            )
        )

        assert(fee == 42.0)
    }

    @Test
    fun `accumulates applier fees`() = runTest {
        val accumulateApplier1 = object : FeeApplier {
            override suspend fun apply(exchangeTransaction: ExchangeTransaction): FeeWeight {
                return FeeWeight.Accumulative(exchangeTransaction.baseBalance * 0.01)
            }
        }
        val accumulateApplier2 = object : FeeApplier {
            override suspend fun apply(exchangeTransaction: ExchangeTransaction): FeeWeight {
                return FeeWeight.Accumulative(10.0)
            }
        }

        val fee = createResolver(setOf(accumulateApplier1, accumulateApplier2)).resolve(
            ExchangeTransaction(
                base = BASE,
                rate = ExchangeRate(TARGET, 1.21),
                baseBalance = 42.0,
                amount = 3.0,
            )
        )

        assert(fee == (0.42 + 10))
    }

    private fun createResolver(appliers: Set<FeeApplier>): FeeResolverImpl {
        return FeeResolverImpl(appliers)
    }

    private companion object {
        val BASE = Currency("EUR")
        val TARGET = Currency("USD")
    }
}
