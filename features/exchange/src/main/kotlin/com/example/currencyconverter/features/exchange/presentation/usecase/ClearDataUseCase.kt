package com.example.currencyconverter.features.exchange.presentation.usecase

import com.example.currencyconverter.core.balance.repository.BalanceRepository
import com.example.currencyconverter.core.exchange.rates.model.Currency
import javax.inject.Inject

internal class ClearDataUseCase @Inject constructor(
    private val balanceRepository: BalanceRepository,
) {
    suspend fun execute() {
        balanceRepository.clearAll()
        balanceRepository.addToBalance(Currency("EUR"), 1000.0)
    }
}
