package com.example.currencyconverter.features.exchange.presentation

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.core.exchange.converter.error.ExchangeError
import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.repository.ExchangeRatesRepository
import com.example.currencyconverter.features.exchange.presentation.ExchangeUiState.ExchangeStatus
import com.example.currencyconverter.features.exchange.presentation.usecase.ExchangeCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class ExchangeViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val exchangeCurrencyUseCase: ExchangeCurrencyUseCase,
    private val exchangeRatesRepository: ExchangeRatesRepository,
) : ViewModel(), DefaultLifecycleObserver {

    private val _state = MutableStateFlow(ExchangeUiState())
    val state: StateFlow<ExchangeUiState> = _state

    private val refreshJob: Job = Job()

    fun clearData() {
        // clear balances and counters
    }

    fun exchangeCurrency(base: Currency, amount: Double, targetCurrency: Currency) {
        viewModelScope.launch {
            _state.update { it.copy(exchangeStatus = ExchangeStatus.Loading) }
            withContext(ioDispatcher) {
                exchangeCurrencyUseCase.execute(
                    base = base,
                    target = targetCurrency,
                    amount = amount
                )
            }
                .fold(
                    onSuccess = { result ->
                        _state.update {
                            it.copy(
                                exchangeStatus = ExchangeStatus.Success(
                                    currencyTraded = result.oldBaseBalance - result.newBaseBalance,
                                    currencyBought = result.convertedAmount,
                                    fee = result.fee,
                                )
                            )
                        }
                    },
                    onFailure = { throwable ->
                        val status = if (throwable is ExchangeError) {
                            when (throwable) {
                                is ExchangeError.FeeTooHigh -> ExchangeStatus.ErrorFeeTooHigh
                            }
                        } else {
                            ExchangeStatus.Idle
                        }
                        _state.update { it.copy(exchangeStatus = status) }
                    },
                )
        }
    }

    private fun refreshRates() {
        viewModelScope.launch(refreshJob) {
            while (isActive) {
                exchangeRatesRepository.refreshRates()
                delay(REFRESH_DELAY_MS)
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        refreshRates()
    }

    override fun onPause(owner: LifecycleOwner) {
        refreshJob.cancel()
    }

    private companion object {
        const val REFRESH_DELAY_MS = 5_000L
    }
}
