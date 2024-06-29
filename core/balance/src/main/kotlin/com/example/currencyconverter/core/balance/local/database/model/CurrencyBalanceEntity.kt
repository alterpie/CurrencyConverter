package com.example.currencyconverter.core.balance.local.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.currencyconverter.core.exchange.rates.model.Currency

@Entity(tableName = CurrencyBalanceEntity.TABLE_NAME, primaryKeys = ["currency"])
internal data class CurrencyBalanceEntity(
    @ColumnInfo(name = COLUMN_CURRENCY) val currency: Currency,
    @ColumnInfo(name = COLUMN_AMOUNT) val amount: Double,
) {
    companion object {
        const val TABLE_NAME = "currency_balance"
        const val COLUMN_CURRENCY = "currency"
        const val COLUMN_AMOUNT = "amount"
    }
}
