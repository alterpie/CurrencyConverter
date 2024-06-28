package com.example.currencyconverter.core.exchange.fees

import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import javax.inject.Inject

internal class TradedCurrencyFee @Inject constructor() : FeeApplier {

    override fun apply(exchangeTransaction: ExchangeTransaction): FeeWeight {
        return FeeWeight.Accumulative(exchangeTransaction.baseBalance * 0.07)
    }
}