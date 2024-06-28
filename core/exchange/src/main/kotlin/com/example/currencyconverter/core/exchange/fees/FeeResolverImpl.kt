package com.example.currencyconverter.core.exchange.fees

import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import javax.inject.Inject

internal class FeeResolverImpl @Inject constructor(
    private val feeAppliers: Set<FeeApplier>,
) : FeeResolver {

    override fun resolve(exchangeTransaction: ExchangeTransaction): Double {
        var totalFee = 0.0
        for (applier in feeAppliers) {
            when (val weight = applier.apply(exchangeTransaction)) {
                is FeeWeight.Accumulative -> {
                    totalFee += weight.amount
                }

                FeeWeight.NotValid -> continue
                is FeeWeight.SubstituteAll -> {
                    return weight.amount
                }
            }
        }
        return totalFee
    }
}
