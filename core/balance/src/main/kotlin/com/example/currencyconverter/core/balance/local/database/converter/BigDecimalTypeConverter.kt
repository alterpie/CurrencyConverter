package com.example.currencyconverter.core.balance.local.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

internal class BigDecimalTypeConverter {

    @TypeConverter
    fun toRawString(value: BigDecimal): String {
        return value.toPlainString()
    }

    @TypeConverter
    fun toBigDecimal(rawString: String): BigDecimal {
        return BigDecimal(rawString)
    }
}
