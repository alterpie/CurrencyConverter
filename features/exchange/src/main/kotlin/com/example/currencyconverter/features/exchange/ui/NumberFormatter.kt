package com.example.currencyconverter.features.exchange.ui

import java.util.Locale

internal object NumberFormatter {

    fun format(value: Double): String {
        val fractionDigitCount = if ((value * 100).toInt() > 0) {
            2
        } else {
            6
        }
        return String.format(Locale.getDefault(), "%.${fractionDigitCount}f", value)
    }
}
