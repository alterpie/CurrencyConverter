package com.example.currencyconverter.core.exchange.rates.mappers

import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import com.example.currencyconverter.core.exchange.rates.remote.model.ExchangeRatesDto

internal fun ExchangeRatesDto.map(): List<ExchangeRate> {
    return rates.map { ExchangeRate(Currency(it.key), it.value) }
}
