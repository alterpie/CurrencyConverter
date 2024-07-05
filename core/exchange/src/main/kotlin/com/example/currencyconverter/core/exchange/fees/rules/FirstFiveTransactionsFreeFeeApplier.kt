package com.example.currencyconverter.core.exchange.fees.rules

import com.example.currencyconverter.core.exchange.attempts.ExchangeAttemptsRepository
import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import com.example.currencyconverter.core.exchange.fees.FeeApplier
import com.example.currencyconverter.core.exchange.fees.FeeWeight
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import java.math.BigDecimal
import javax.inject.Inject

internal class FirstFiveTransactionsFreeFeeApplier @Inject constructor(
    private val exchangeAttemptsRepository: ExchangeAttemptsRepository,
) : FeeApplier {

    override suspend fun apply(exchangeTransaction: ExchangeTransaction): FeeWeight {
        return exchangeAttemptsRepository.getExchangeAttemptsCount()
            .take(1)
            .map { count ->
                if (count < 5) {
                    exchangeAttemptsRepository.incrementExchangeAttemptsCount()
                    FeeWeight.SubstituteAll(BigDecimal.ZERO)
                } else {
                    FeeWeight.NotValid
                }
            }
            .first()
    }
}
