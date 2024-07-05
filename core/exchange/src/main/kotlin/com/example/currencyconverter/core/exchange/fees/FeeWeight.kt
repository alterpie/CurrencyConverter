package com.example.currencyconverter.core.exchange.fees

import java.math.BigDecimal

internal sealed interface FeeWeight {
    data class SubstituteAll(val amount: BigDecimal) : FeeWeight
    data class Accumulative(val amount: BigDecimal) : FeeWeight
    data object NotValid : FeeWeight
}
