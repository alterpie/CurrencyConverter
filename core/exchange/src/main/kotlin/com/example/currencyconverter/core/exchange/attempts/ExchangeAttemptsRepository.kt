package com.example.currencyconverter.core.exchange.attempts

import kotlinx.coroutines.flow.Flow

interface ExchangeAttemptsRepository {

    fun getExchangeAttemptsCount(): Flow<Int>
    suspend fun incrementExchangeAttemptsCount()
    suspend fun clear()
}
