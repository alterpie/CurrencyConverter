package com.example.currencyconverter.core.exchange.fees

internal sealed interface FeeWeight {
    data class SubstituteAll(val amount: Double) : FeeWeight
    data class Accumulative(val amount: Double) : FeeWeight
    data object NotValid : FeeWeight
}
