package com.example.currencyconverter.core.exchange.fees

import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import java.math.BigDecimal
import javax.inject.Inject

internal class FeeResolverImpl @Inject constructor(
    private val feeAppliers: Set<@JvmSuppressWildcards FeeApplier>,
) : FeeResolver {

    override suspend fun resolve(exchangeTransaction: ExchangeTransaction): BigDecimal {
        var totalFee = BigDecimal.ZERO
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
