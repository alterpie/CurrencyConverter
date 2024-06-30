package com.example.currencyconverter.core.design_system.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

internal val DarkColors = darkColors()
internal val LightColors = lightColors(
    primary = Color(0, 156, 222),
    onPrimary = Color.White,
)

val Colors.textPrimary
    get() = if (isLight) {
        Color.Black
    } else {
        Color.White
    }

val Colors.textSecondary
    get() = if (isLight) {
        Color(126, 132, 144)
    } else {
        Color(175, 180, 190, 255)
    }

val IndicationNegative = Color(240, 81, 82)
val IndicationPositive = Color(14, 159, 110)
