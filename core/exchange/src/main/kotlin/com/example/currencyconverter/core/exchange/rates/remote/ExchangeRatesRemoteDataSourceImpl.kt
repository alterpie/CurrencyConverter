package com.example.currencyconverter.core.exchange.rates.remote

import com.example.currencyconverter.core.exchange.rates.remote.model.ExchangeRatesDto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import javax.inject.Inject

internal class ExchangeRatesRemoteDataSourceImpl @Inject constructor(
    private val exchangeRatesApi: ExchangeRatesApi,
) : ExchangeRatesRemoteDataSource {

    override suspend fun getRates(): Result<ExchangeRatesDto> {
        return kotlin.runCatching {
            exchangeRatesApi.getExchangeRates()
        }.onFailure { throwable ->
            if (throwable is CancellationException && throwable !is TimeoutCancellationException) {
                throw throwable
            }
        }
    }
}