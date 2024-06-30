package com.example.currencyconverter.core.exchange.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.currencyconverter.core.exchange.attempts.ExchangeAttemptsRepository
import com.example.currencyconverter.core.exchange.attempts.ExchangeAttemptsRepositoryImpl
import com.example.currencyconverter.core.exchange.converter.ExchangeEngine
import com.example.currencyconverter.core.exchange.converter.ExchangeEngineImpl
import com.example.currencyconverter.core.exchange.fees.FeeApplier
import com.example.currencyconverter.core.exchange.fees.FeeResolver
import com.example.currencyconverter.core.exchange.fees.FeeResolverImpl
import com.example.currencyconverter.core.exchange.fees.FirstFiveTransactionsFreeFeeApplier
import com.example.currencyconverter.core.exchange.fees.TradedCurrencyFee
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface ExchangeModule {

    @Binds
    fun feeResolver(impl: FeeResolverImpl): FeeResolver

    @Binds
    fun exchangeEngine(impl: ExchangeEngineImpl): ExchangeEngine

    @Binds
    fun exchangeAttemptsRepository(impl: ExchangeAttemptsRepositoryImpl): ExchangeAttemptsRepository

    companion object {

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "exchange_data_store")

        @Provides
        @IntoSet
        fun firstFiveTransactionsFreeFeeApplier(
            exchangeAttemptsRepository: ExchangeAttemptsRepository,
        ): FeeApplier {
            return FirstFiveTransactionsFreeFeeApplier(exchangeAttemptsRepository)
        }

        @Provides
        @IntoSet
        fun tradeCurrencyFee(): FeeApplier {
            return TradedCurrencyFee()
        }

        @Provides
        @Singleton
        fun dataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.dataStore
        }
    }
}
