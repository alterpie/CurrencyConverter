package com.example.currencyconverter.core.balance.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.currencyconverter.core.currency.model.Currency

@Entity(tableName = "currency_balance")
internal data class CurrencyBalanceEntity(
    @ColumnInfo(name = "currency") val currency: Currency,
    @ColumnInfo(name = "amount") val amount: Double,
)
