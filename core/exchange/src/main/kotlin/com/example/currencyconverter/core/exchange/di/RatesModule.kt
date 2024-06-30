package com.example.currencyconverter.core.exchange.di

import com.example.currencyconverter.core.exchange.BuildConfig
import com.example.currencyconverter.core.exchange.rates.remote.ExchangeRatesApi
import com.example.currencyconverter.core.exchange.rates.remote.ExchangeRatesRemoteDataSource
import com.example.currencyconverter.core.exchange.rates.remote.ExchangeRatesRemoteDataSourceImpl
import com.example.currencyconverter.core.exchange.rates.repository.ExchangeRatesRepository
import com.example.currencyconverter.core.exchange.rates.repository.ExchangeRatesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal interface RatesModule {

    @Singleton
    @Binds
    fun exchangeRatesRepository(impl: ExchangeRatesRepositoryImpl): ExchangeRatesRepository

    @Binds
    fun exchangeRatesRemoteDataSource(impl: ExchangeRatesRemoteDataSourceImpl): ExchangeRatesRemoteDataSource

    companion object {

        @Provides
        @Singleton
        fun okhttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .apply {
                    if (BuildConfig.DEBUG) {
                        addInterceptor(
                            HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            }
                        )
                    }
                }
                .build()
        }

        @Provides
        @Singleton
        fun retrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://developers.paysera.com")
                .client(okHttpClient)
                .addConverterFactory(
                    Json.asConverterFactory("application/json; charset=UTF8".toMediaType())
                )
                .build()
        }

        @Provides
        @Singleton
        fun exchangeRatesApi(retrofit: Retrofit): ExchangeRatesApi {
            return retrofit
                .create(ExchangeRatesApi::class.java)
        }
    }
}