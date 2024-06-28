package com.example.currencyconverter.core.exchange.rates.remote

import com.example.currencyconverter.core.exchange.rates.remote.model.ExchangeRatesDto

internal interface ExchangeRatesRemoteDataSource {

    suspend fun getRates(): Result<ExchangeRatesDto>
}