package com.example.currencyconverter.core.exchange.fees

import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import javax.inject.Inject

internal class FirstFiveTransactionsFreeFeeApplier @Inject constructor() : FeeApplier {

    private var count = 0

    override fun apply(exchangeTransaction: ExchangeTransaction): FeeWeight {
        return if (count < 5) {
            count++
            FeeWeight.SubstituteAll(0.0)
        } else {
            FeeWeight.NotValid
        }
    }
}
