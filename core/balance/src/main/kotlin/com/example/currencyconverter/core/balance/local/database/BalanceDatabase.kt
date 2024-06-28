package com.example.currencyconverter.core.balance.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyconverter.core.balance.local.database.dao.CurrencyBalanceDao
import com.example.currencyconverter.core.balance.local.database.model.CurrencyBalanceEntity

@Database(
    entities = [CurrencyBalanceEntity::class],
    version = 1,
)
internal abstract class BalanceDatabase : RoomDatabase() {

    abstract fun currencyBalanceDao(): CurrencyBalanceDao
}