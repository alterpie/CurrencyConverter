package com.example.currencyconverter.features.exchange.presentation

import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal

internal data class ExchangeUiState(
    val baseCurrency: Currency = Currency("EUR"),
    val balances: ImmutableList<CurrencyBalance> = persistentListOf(),
    val rates: ImmutableList<ExchangeRate> = persistentListOf(),
    val ratesStatus: RatesStatus = RatesStatus.CONTENT,
    val exchangeStatus: ExchangeStatus = ExchangeStatus.Idle,
    val selectedBalance: CurrencyBalance? = null,
    val selectedRate: ExchangeRate? = null,
    val exchangeAmount: BigDecimal? = null,
) {
    enum class RatesStatus {
        LOADING, FAILURE, CONTENT;
    }

    sealed interface ExchangeStatus {
        data object Idle : ExchangeStatus
        data object Loading : ExchangeStatus
        data object ErrorFeeTooHigh : ExchangeStatus
        data object ErrorNotEnoughBalance : ExchangeStatus
        data class Success(
            val traded: Pair<Currency, BigDecimal>,
            val bought: Pair<Currency, BigDecimal>,
            val fee: Pair<Currency, BigDecimal>?,
        ) : ExchangeStatus
    }
}
