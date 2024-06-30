package com.example.currencyconverter.features.exchange.presentation.usecase

import com.example.currencyconverter.core.balance.repository.BalanceRepository
import com.example.currencyconverter.core.exchange.attempts.ExchangeAttemptsRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class ClearDataUseCaseTest {

    private val balanceRepository = mockk<BalanceRepository>()
    private val exchangeAttemptsRepository = mockk<ExchangeAttemptsRepository>()

    @Test
    fun `clears local data`() = runTest {
        ClearDataUseCase(
            balanceRepository = balanceRepository,
            exchangeAttemptsRepository = exchangeAttemptsRepository,
        ).execute()

        coVerify {
            balanceRepository.clearAll()
            exchangeAttemptsRepository.clear()
        }
    }
}