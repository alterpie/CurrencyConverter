package com.example.currencyconverter.core.exchange.converter

import com.example.currencyconverter.core.exchange.converter.error.ExchangeError
import com.example.currencyconverter.core.exchange.converter.model.ExchangeResult
import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import com.example.currencyconverter.core.exchange.fees.FeeResolver
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

internal class ExchangeEngineImpl @Inject constructor(
    private val feeResolver: FeeResolver,
) : ExchangeEngine {

    override suspend fun convert(exchangeTransaction: ExchangeTransaction): Result<ExchangeResult> {
        if (exchangeTransaction.amount > exchangeTransaction.baseBalance.setScale(
                2,
                RoundingMode.HALF_DOWN
            )
        ) {
            return Result.failure(ExchangeError.NotEnoughBalance())
        }
        val feeAmount = feeResolver.resolve(exchangeTransaction)
        return if (feeAmount > exchangeTransaction.baseBalance) {
            Result.failure(ExchangeError.FeeTooHigh())
        } else {
            val converted = convert(exchangeTransaction.amount, exchangeTransaction.rate.value)
            Result.success(
                ExchangeResult(
                    tradedAmount = exchangeTransaction.amount,
                    convertedAmount = converted,
                    fee = feeAmount,
                )
            )
        }
    }

    private fun convert(amount: BigDecimal, rate: BigDecimal): BigDecimal {
        return amount.multiply(rate)
    }
}
