package com.example.currencyconverter.core.exchange.rates.remote

import com.example.currencyconverter.core.exchange.rates.remote.model.ExchangeRatesDto
import retrofit2.http.GET

internal interface ExchangeRatesApi {

    @GET("/tasks/api/currency-exchange-rates")
    suspend fun getExchangeRates(): ExchangeRatesDto
}
