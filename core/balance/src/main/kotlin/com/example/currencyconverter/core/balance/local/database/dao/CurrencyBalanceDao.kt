package com.example.currencyconverter.core.balance.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.currencyconverter.core.balance.local.database.model.CurrencyBalanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CurrencyBalanceDao {

    @Query("SELECT * FROM currency_balance")
    fun getBalances(): Flow<List<CurrencyBalanceEntity>>

    @Upsert
    suspend fun updateBalance(currencyBalanceEntity: CurrencyBalanceEntity)

    @Query("DELETE FROM currency_balance")
    suspend fun deleteAll()
}
