package com.example.currencyconverter.core.balance.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.currencyconverter.core.exchange.rates.model.Currency

@Entity(tableName = "currency_balance", primaryKeys = ["currency"])
internal data class CurrencyBalanceEntity(
    @ColumnInfo(name = "currency") val currency: Currency,
    @ColumnInfo(name = "amount") val amount: Double,
)
