package com.example.currencyconverter.core.exchange.attempts

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ExchangeAttemptsDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    fun getExchangeAttemptsCount(): Flow<Int> {
        return dataStore.data
            .map { preferences -> preferences[KEY_ATTEMPTS_COUNT] ?: 0 }
    }

    suspend fun incrementExchangeAttemptsCount() {
        dataStore.edit { preferences ->
            val current = preferences[KEY_ATTEMPTS_COUNT] ?: 0
            preferences[KEY_ATTEMPTS_COUNT] = current.inc()
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences -> preferences.clear() }
    }

    companion object {
        val KEY_ATTEMPTS_COUNT = intPreferencesKey("attempts_count")
    }
}