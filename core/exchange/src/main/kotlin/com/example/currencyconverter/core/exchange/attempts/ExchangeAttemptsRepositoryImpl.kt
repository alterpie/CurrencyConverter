package com.example.currencyconverter.core.exchange.attempts

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ExchangeAttemptsRepositoryImpl @Inject constructor(
    private val exchangeAttemptsDataSource: ExchangeAttemptsDataSource,
) : ExchangeAttemptsRepository {

    override fun getExchangeAttemptsCount(): Flow<Int> {
        return exchangeAttemptsDataSource.getExchangeAttemptsCount()
    }

    override suspend fun incrementExchangeAttemptsCount() {
       exchangeAttemptsDataSource.incrementExchangeAttemptsCount()
    }

    override suspend fun clear() {
        exchangeAttemptsDataSource.clear()
    }
}