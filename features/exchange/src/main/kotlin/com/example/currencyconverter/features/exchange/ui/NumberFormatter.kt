package com.example.currencyconverter.features.exchange.ui

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

internal object NumberFormatter {

    private val format = DecimalFormat.getInstance()

    fun format(value: BigDecimal): String {
        val fractionDigitCount = if ((value * BigDecimal(100.0)).toInt() > 0) {
            2
        } else {
            6
        }
        format.apply {
            maximumFractionDigits = fractionDigitCount
            minimumFractionDigits = fractionDigitCount
            roundingMode = RoundingMode.DOWN
        }
        return format.format(value)
    }
}
