package com.example.currencyconverter.core.currency.repository

import com.example.currencyconverter.core.currency.model.ExchangeRate
import kotlinx.coroutines.flow.Flow

interface ExchangeRatesRepository {

    fun getRates(): Flow<List<ExchangeRate>>

    suspend fun refreshRates(): Result<Unit>
}
