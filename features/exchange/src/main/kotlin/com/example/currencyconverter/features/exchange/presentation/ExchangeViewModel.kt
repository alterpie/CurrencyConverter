package com.example.currencyconverter.features.exchange.presentation

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.balance.repository.BalanceRepository
import com.example.currencyconverter.core.exchange.converter.error.ExchangeError
import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import com.example.currencyconverter.core.exchange.rates.repository.ExchangeRatesRepository
import com.example.currencyconverter.features.exchange.presentation.ExchangeUiState.ExchangeStatus
import com.example.currencyconverter.features.exchange.presentation.ExchangeUiState.RatesStatus
import com.example.currencyconverter.features.exchange.presentation.usecase.ClearDataUseCase
import com.example.currencyconverter.features.exchange.presentation.usecase.ExchangeCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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
    private val clearDataUseCase: ClearDataUseCase,
) : ViewModel(), DefaultLifecycleObserver {

    private val _state = MutableStateFlow(ExchangeUiState())
    val state: StateFlow<ExchangeUiState> = _state

    private var refreshJob: Job = Job()
    private val amountInput = MutableStateFlow("")
    private val allRates = MutableStateFlow<Map<Currency, ExchangeRate>>(emptyMap())

    init {
        getAllRates()
        getBalances()
        updateExchangedAmount()
        updateApplicableRates()
    }

    private fun getAllRates() {
        exchangeRatesRepository.getRates()
            .onEach { rates ->
                allRates.update { rates.associate { it.currency to it } }
                _state.update {
                    it.copy(
                        ratesStatus = RatesStatus.CONTENT,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getBalances() {
        balanceRepository.getBalances()
            .onEach { balances ->
                val selectedBalance = _state.value.selectedBalance ?: balances.firstOrNull()
                _state.update {
                    it.copy(
                        balances = balances.toImmutableList(),
                        selectedBalance = selectedBalance,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onBalanceSelected(currencyBalance: CurrencyBalance) {
        _state.update { it.copy(selectedBalance = currencyBalance) }
    }

    fun onRateSelected(exchangeRate: ExchangeRate) {
        _state.update { it.copy(selectedRate = exchangeRate) }
    }

    fun onExchangeAmountChange(amountInput: String) {
        this.amountInput.value = amountInput
    }

    private fun updateApplicableRates() {
        combine(
            allRates.filter { it.isNotEmpty() },
            state.filter { it.selectedBalance != null },
        ) { rates, state ->
            val selectedBalance = requireNotNull(state.selectedBalance)

            val updatedRates = if (selectedBalance.currency == state.baseCurrency) {
                rates
            } else {
                val rateToBase = requireNotNull(rates[selectedBalance.currency])
                mapOf(state.baseCurrency to ExchangeRate(state.baseCurrency, 1 / rateToBase.value))
            }
            val selectedRate = if (updatedRates[state.baseCurrency] == null) {
                val currentlySelected = state.selectedRate
                if (currentlySelected == null || currentlySelected.currency == state.baseCurrency) {
                    updatedRates.entries.first().value
                } else {
                    currentlySelected
                }
            } else {
                updatedRates[state.selectedRate?.currency] ?: updatedRates.entries.first().value
            }

            updatedRates.values to selectedRate
        }
            .onEach { (updatedRates, selectedRate) ->
                _state.update {
                    it.copy(
                        rates = updatedRates.toImmutableList(),
                        selectedRate = selectedRate,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun updateExchangedAmount() {
        combine(
            amountInput.map { it.toDoubleOrNull() },
            _state
                .map { it.selectedRate }
                .filterNotNull()
        ) { amount, rate ->

            _state.update { it.copy(exchangeAmount = amount?.let { it * rate.value }) }
        }
            .launchIn(viewModelScope)
    }

    fun clearExchangeStatus() {
        _state.update { it.copy(exchangeStatus = ExchangeStatus.Idle) }
    }

    fun exchangeCurrency(amountInput: String) {
        val base = _state.value.selectedBalance?.currency ?: return
        val targetRate = _state.value.selectedRate ?: return
        viewModelScope.launch {
            _state.update { it.copy(exchangeStatus = ExchangeStatus.Loading) }
            val amount = amountInput.toDouble()
            // in production io dispatcher should be injected in ViewModel constructor,
            // use directly here for simplicity
            withContext(Dispatchers.IO) {
                exchangeCurrencyUseCase.execute(
                    base = base,
                    rate = targetRate,
                    amount = amount,
                )
            }
                .fold(
                    onSuccess = { result ->
                        _state.update {
                            it.copy(
                                exchangeStatus = ExchangeStatus.Success(
                                    traded = base to result.tradedAmount,
                                    bought = targetRate.currency to result.convertedAmount,
                                    fee = base to result.fee,
                                )
                            )
                        }
                    },
                    onFailure = { throwable ->
                        val status = if (throwable is ExchangeError) {
                            when (throwable) {
                                is ExchangeError.FeeTooHigh -> ExchangeStatus.ErrorFeeTooHigh
                                is ExchangeError.NotEnoughBalance -> ExchangeStatus.ErrorNotEnoughBalance
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
        refreshJob = viewModelScope.launch {
            while (isActive) {
                exchangeRatesRepository.refreshRates()
                    .onFailure {
                        if (_state.value.rates.isEmpty()) {
                            _state.update { it.copy(ratesStatus = RatesStatus.FAILURE) }
                        }
                    }
                delay(REFRESH_DELAY_MS)
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        if (_state.value.rates.isEmpty()) {
            _state.update { it.copy(ratesStatus = RatesStatus.LOADING) }
        }
        refreshRates()
    }

    override fun onPause(owner: LifecycleOwner) {
        refreshJob.cancel()
    }

    fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            clearDataUseCase.execute()
        }
    }

    private companion object {
        const val REFRESH_DELAY_MS = 5_000L
    }
}
