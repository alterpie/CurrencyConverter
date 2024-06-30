package com.example.currencyconverter.features.exchange.presentation

import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class ExchangeUiState(
    val balances: ImmutableList<CurrencyBalance> = persistentListOf(),
    val rates: ImmutableList<ExchangeRate> = persistentListOf(),
    val screenStatus: ScreenStatus = ScreenStatus.CONTENT,
    val exchangeStatus: ExchangeStatus = ExchangeStatus.Idle,
    val selectedBalance: CurrencyBalance? = null,
    val selectedRate: ExchangeRate? = null,
    val exchangeAmount: Double? = null,
) {
    enum class ScreenStatus {
        LOADING, FAILURE, CONTENT;
    }

    sealed interface ExchangeStatus {
        data object Idle : ExchangeStatus
        data object Loading : ExchangeStatus
        data object ErrorFeeTooHigh : ExchangeStatus
        data object ErrorNotEnoughBalance : ExchangeStatus
        data class Success(
            val traded: Pair<Currency, Double>,
            val bought: Pair<Currency, Double>,
            val fee: Pair<Currency, Double>?,
        ) : ExchangeStatus
    }
}
