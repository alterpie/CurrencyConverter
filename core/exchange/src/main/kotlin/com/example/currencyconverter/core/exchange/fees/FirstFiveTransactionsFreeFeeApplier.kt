package com.example.currencyconverter.core.exchange.fees

import com.example.currencyconverter.core.exchange.attempts.ExchangeAttemptsRepository
import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
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
                    FeeWeight.SubstituteAll(0.0)
                } else {
                    FeeWeight.NotValid
                }
            }
            .first()
    }
}
