package com.example.currencyconverter.core.currency.repository

import com.example.currencyconverter.core.currency.mappers.map
import com.example.currencyconverter.core.currency.model.ExchangeRate
import com.example.currencyconverter.core.currency.remote.ExchangeRatesRemoteDataSource
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