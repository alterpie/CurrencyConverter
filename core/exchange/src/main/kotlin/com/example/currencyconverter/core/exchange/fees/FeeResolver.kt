package com.example.currencyconverter.core.exchange.fees

import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction

internal interface FeeResolver {

    /**
         * Calculates fee amount for provided [ExchangeTransaction].
     */
    fun resolve(exchangeTransaction: ExchangeTransaction): Double
}
