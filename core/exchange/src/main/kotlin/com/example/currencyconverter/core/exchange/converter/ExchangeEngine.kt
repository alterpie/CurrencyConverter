package com.example.currencyconverter.core.exchange.converter

import com.example.currencyconverter.core.exchange.converter.model.ExchangeResult
import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction

interface ExchangeEngine {

    /**
     * Converts currency amount based on provided  [ExchangeTransaction].
     */
    suspend fun convert(exchangeTransaction: ExchangeTransaction): Result<ExchangeResult>
}
