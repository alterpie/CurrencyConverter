package com.example.currencyconverter.core.exchange.fees

import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import java.math.BigDecimal

internal interface FeeResolver {

    /**
     * Calculates fee amount for provided [ExchangeTransaction].
     */
    suspend fun resolve(exchangeTransaction: ExchangeTransaction): BigDecimal
}
