package com.example.currencyconverter.core.balance.di

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.currencyconverter.core.balance.local.BalanceLocalDataSource
import com.example.currencyconverter.core.balance.local.BalanceLocalDataSourceImpl
import com.example.currencyconverter.core.balance.local.database.BalanceDatabase
import com.example.currencyconverter.core.balance.local.database.dao.CurrencyBalanceDao
import com.example.currencyconverter.core.balance.local.database.model.CurrencyBalanceEntity
import com.example.currencyconverter.core.balance.repository.BalanceRepository
import com.example.currencyconverter.core.balance.repository.BalanceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
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
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        Executors.newSingleThreadExecutor().execute {
                            val values = ContentValues().apply {
                                put(CurrencyBalanceEntity.COLUMN_CURRENCY, "EUR")
                                put(CurrencyBalanceEntity.COLUMN_AMOUNT, 1000.0)
                            }
                            db.insert(
                                CurrencyBalanceEntity.TABLE_NAME,
                                SQLiteDatabase.CONFLICT_REPLACE,
                                values,
                            );
                        }
                    }
                })
                .build()

        }

        @Provides
        fun currencyBalanceDao(database: BalanceDatabase): CurrencyBalanceDao {
            return database.currencyBalanceDao()
        }
    }
}
