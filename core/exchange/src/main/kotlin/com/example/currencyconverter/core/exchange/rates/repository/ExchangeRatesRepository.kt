package com.example.currencyconverter.core.exchange.rates.repository

import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface ExchangeRatesRepository {

    fun getRates(): Flow<List<ExchangeRate>>

    suspend fun refreshRates(): Result<Unit>
}
