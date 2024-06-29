package com.example.currencyconverter.features.exchange.presentation

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.balance.repository.BalanceRepository
import com.example.currencyconverter.core.exchange.converter.error.ExchangeError
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import com.example.currencyconverter.core.exchange.rates.repository.ExchangeRatesRepository
import com.example.currencyconverter.features.exchange.presentation.ExchangeUiState.ExchangeStatus
import com.example.currencyconverter.features.exchange.presentation.ExchangeUiState.ScreenStatus
import com.example.currencyconverter.features.exchange.presentation.usecase.ExchangeCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class ExchangeViewModel @Inject constructor(
    private val exchangeCurrencyUseCase: ExchangeCurrencyUseCase,
    private val exchangeRatesRepository: ExchangeRatesRepository,
    private val balanceRepository: BalanceRepository,
) : ViewModel(), DefaultLifecycleObserver {

    private val _state = MutableStateFlow(ExchangeUiState())
    val state: StateFlow<ExchangeUiState> = _state

    private val refreshJob: Job = Job()

    init {
        getRates()
        getBalances()
    }

    private fun getRates() {
        exchangeRatesRepository.getRates()
            .onEach { rates ->
                _state.update {
                    it.copy(
                        rates = rates.toImmutableList(),
                        screenStatus = ScreenStatus.CONTENT,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getBalances() {
        balanceRepository.getBalances()
            .onEach { balances -> _state.update { it.copy(balances = balances.toImmutableList()) } }
            .launchIn(viewModelScope)
    }

    fun onBalanceSelected(currencyBalance: CurrencyBalance) {
        _state.update { it.copy(selectedBalance = currencyBalance) }
    }

    fun onRateSelected(exchangeRate: ExchangeRate) {
        _state.update { it.copy(selectedRate = exchangeRate) }
    }

    fun onExchangeAmountChange(amountInput: String) {
        _state.update { it.copy(exchangeAmount = amountInput.toDouble()) }
    }

    fun clearExchangeStatus() {
        _state.update { it.copy(exchangeStatus = ExchangeStatus.Idle) }
    }

    fun exchangeCurrency(amountInput: String) {
        val base = _state.value.selectedBalance?.currency ?: return
        val targetCurrency = _state.value.selectedRate?.currency ?: return
        viewModelScope.launch {
            _state.update { it.copy(exchangeStatus = ExchangeStatus.Loading) }
            val amount = amountInput.toDouble()
            withContext(Dispatchers.IO) {
                exchangeCurrencyUseCase.execute(
                    base = base,
                    target = targetCurrency,
                    amount = amount,
                )
            }
                .fold(
                    onSuccess = { result ->
                        _state.update {
                            it.copy(
                                exchangeStatus = ExchangeStatus.Success(
                                    traded = result.baseCurrency to (result.oldBaseBalance - result.newBaseBalance),
                                    bought = result.convertedCurrency to result.convertedAmount,
                                    fee = result.feeCurrency to result.fee,
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
                    .onFailure {
                        if (_state.value.rates.isEmpty()) {
                            _state.update { it.copy(screenStatus = ScreenStatus.FAILURE) }
                        }
                    }
                delay(REFRESH_DELAY_MS)
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        if (_state.value.rates.isEmpty()) {
            _state.update { it.copy(screenStatus = ScreenStatus.LOADING) }
        }
        refreshRates()
    }

    override fun onPause(owner: LifecycleOwner) {
        refreshJob.cancel()
    }

    fun clearData() {
        // clear balances and counters
    }

    private companion object {
        const val REFRESH_DELAY_MS = 5_000L
    }
}
