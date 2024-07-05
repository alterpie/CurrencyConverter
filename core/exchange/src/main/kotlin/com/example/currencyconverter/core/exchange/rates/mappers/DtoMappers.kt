package com.example.currencyconverter.core.exchange.rates.mappers

import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import com.example.currencyconverter.core.exchange.rates.remote.model.ExchangeRatesDto
import java.math.BigDecimal

internal fun ExchangeRatesDto.map(): List<ExchangeRate> {
    return rates.map { ExchangeRate(Currency(it.key), BigDecimal(it.value)) }
}
