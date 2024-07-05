package com.example.currencyconverter.core.balance.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.currencyconverter.core.balance.local.database.converter.BigDecimalTypeConverter
import com.example.currencyconverter.core.balance.local.database.dao.CurrencyBalanceDao
import com.example.currencyconverter.core.balance.local.database.model.CurrencyBalanceEntity

@Database(
    entities = [CurrencyBalanceEntity::class],
    version = 1,
)
@TypeConverters(BigDecimalTypeConverter::class)
internal abstract class BalanceDatabase : RoomDatabase() {

    abstract fun currencyBalanceDao(): CurrencyBalanceDao
}