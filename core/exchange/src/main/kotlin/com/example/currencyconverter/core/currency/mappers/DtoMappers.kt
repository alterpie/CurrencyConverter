package com.example.currencyconverter.core.currency.mappers

import com.example.currencyconverter.core.currency.model.Currency
import com.example.currencyconverter.core.currency.model.ExchangeRate
import com.example.currencyconverter.core.currency.remote.model.ExchangeRatesDto

internal fun ExchangeRatesDto.map(): List<ExchangeRate> {
    return rates.map { ExchangeRate(Currency(it.key), it.value) }
}
