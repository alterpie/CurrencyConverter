package com.example.currencyconverter.core.balance.di

import android.content.Context
import androidx.room.Room
import com.example.currencyconverter.core.balance.local.BalanceLocalDataSource
import com.example.currencyconverter.core.balance.local.BalanceLocalDataSourceImpl
import com.example.currencyconverter.core.balance.local.database.BalanceDatabase
import com.example.currencyconverter.core.balance.local.database.dao.CurrencyBalanceDao
import com.example.currencyconverter.core.balance.repository.BalanceRepository
import com.example.currencyconverter.core.balance.repository.BalanceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface BalanceModule {

    @Binds
    @Singleton
    fun balanceRepository(impl: BalanceRepositoryImpl): BalanceRepository

    @Binds
    fun balanceLocalDataSource(impl: BalanceLocalDataSourceImpl): BalanceLocalDataSource

    companion object {

        @Provides
        @Singleton
        fun database(@ApplicationContext context: Context): BalanceDatabase {
            return Room.databaseBuilder(
                context,
                BalanceDatabase::class.java,
                "currency-converter-database"
            )
                .build()

        }

        @Provides
        fun currencyBalanceDao(database: BalanceDatabase): CurrencyBalanceDao {
            return database.currencyBalanceDao()
        }
    }
}
