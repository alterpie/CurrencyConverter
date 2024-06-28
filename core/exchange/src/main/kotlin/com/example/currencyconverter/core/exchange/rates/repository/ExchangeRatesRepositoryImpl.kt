package com.example.currencyconverter.core.exchange.rates.repository

import com.example.currencyconverter.core.exchange.rates.mappers.map
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import com.example.currencyconverter.core.exchange.rates.remote.ExchangeRatesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class ExchangeRatesRepositoryImpl @Inject constructor(
    private val exchangeRatesRemoteDataSource: ExchangeRatesRemoteDataSource
) : ExchangeRatesRepository {

    private val rates = MutableStateFlow(emptyList<ExchangeRate>())

    override fun getRates(): Flow<List<ExchangeRate>> = rates

    override suspend fun refreshRates(): Result<Unit> {
        return exchangeRatesRemoteDataSource.getRates()
            .onSuccess { newRates->
                rates.update { newRates.map() }
            }
            .map {  }
    }
}