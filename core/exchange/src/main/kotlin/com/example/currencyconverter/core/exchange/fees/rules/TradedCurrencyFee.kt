package com.example.currencyconverter.core.exchange.fees.rules

import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import com.example.currencyconverter.core.exchange.fees.FeeApplier
import com.example.currencyconverter.core.exchange.fees.FeeWeight
import java.math.BigDecimal
import javax.inject.Inject

internal class TradedCurrencyFee @Inject constructor() : FeeApplier {

    override suspend fun apply(exchangeTransaction: ExchangeTransaction): FeeWeight {
        return FeeWeight.Accumulative(exchangeTransaction.amount * BigDecimal(0.07))
    }
}
