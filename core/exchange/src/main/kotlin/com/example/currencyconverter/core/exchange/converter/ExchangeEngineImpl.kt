package com.example.currencyconverter.core.exchange.converter

import com.example.currencyconverter.core.exchange.converter.error.ExchangeError
import com.example.currencyconverter.core.exchange.converter.model.ExchangeResult
import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import com.example.currencyconverter.core.exchange.fees.FeeResolver
import javax.inject.Inject

internal class ExchangeEngineImpl @Inject constructor(
    private val feeResolver: FeeResolver,
) : ExchangeEngine {

    override fun convert(exchangeTransaction: ExchangeTransaction): Result<ExchangeResult> {
        val feeAmount = feeResolver.resolve(exchangeTransaction)
        return if (feeAmount > exchangeTransaction.baseBalance) {
            Result.failure(ExchangeError.FeeTooHigh())
        } else {
            val converted = convert(exchangeTransaction.amount, exchangeTransaction.rate.value)
            Result.success(
                ExchangeResult(
                    oldBaseBalance = exchangeTransaction.baseBalance,
                    newBaseBalance = exchangeTransaction.baseBalance - exchangeTransaction.amount,
                    convertedAmount = converted,
                    fee = feeAmount,
                )
            )
        }
    }

    private fun convert(amount: Double, rate: Double): Double {
        return amount * rate
    }
}
