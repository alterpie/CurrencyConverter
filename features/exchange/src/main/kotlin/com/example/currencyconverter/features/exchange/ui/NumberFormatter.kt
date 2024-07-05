package com.example.currencyconverter.features.exchange.ui

import java.math.BigDecimal
import java.util.Locale

internal object NumberFormatter {

    fun format(value: BigDecimal): String {
        val fractionDigitCount = if ((value * BigDecimal(100.0)).toInt() > 0) {
            2
        } else {
            6
        }
        return String.format(Locale.getDefault(), "%.${fractionDigitCount}f", value)
    }
}
