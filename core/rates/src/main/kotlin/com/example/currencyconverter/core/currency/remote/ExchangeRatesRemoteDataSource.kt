package com.example.currencyconverter.core.currency.remote

import com.example.currencyconverter.core.currency.remote.model.ExchangeRatesDto

internal interface ExchangeRatesRemoteDataSource {

    suspend fun getRates(): Result<ExchangeRatesDto>
}