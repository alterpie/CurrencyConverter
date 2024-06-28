package com.example.currencyconverter.core.exchange.di

import com.example.currencyconverter.core.exchange.converter.ExchangeEngine
import com.example.currencyconverter.core.exchange.converter.ExchangeEngineImpl
import com.example.currencyconverter.core.exchange.fees.FeeApplier
import com.example.currencyconverter.core.exchange.fees.FeeResolver
import com.example.currencyconverter.core.exchange.fees.FeeResolverImpl
import com.example.currencyconverter.core.exchange.fees.FirstFiveTransactionsFreeFeeApplier
import com.example.currencyconverter.core.exchange.fees.TradedCurrencyFee
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal interface ExchangeModule {

    @Binds
    @IntoSet
    fun firstFiveTransactionsFreeFeeApplier(impl: FirstFiveTransactionsFreeFeeApplier): FeeApplier

    @Binds
    @IntoSet
    fun tradeCurrencyFee(impl: TradedCurrencyFee): FeeApplier

    @Binds
    fun feeResolver(impl: FeeResolverImpl): FeeResolver

    @Binds
    fun exchangeEngine(impl: ExchangeEngineImpl): ExchangeEngine
}
