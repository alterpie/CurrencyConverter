package com.example.currencyconverter.core.exchange.fees

import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction

internal interface FeeApplier {

    fun apply(exchangeTransaction: ExchangeTransaction): FeeWeight
}
