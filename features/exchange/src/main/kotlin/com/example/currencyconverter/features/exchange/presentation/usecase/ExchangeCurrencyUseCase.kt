package com.example.currencyconverter.features.exchange.presentation.usecase

import com.example.currencyconverter.core.balance.repository.BalanceRepository
import com.example.currencyconverter.core.exchange.converter.ExchangeEngine
import com.example.currencyconverter.core.exchange.converter.model.ExchangeResult
import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.repository.ExchangeRatesRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

internal class ExchangeCurrencyUseCase @Inject constructor(
    private val balanceRepository: BalanceRepository,
    private val exchangeRatesRepository: ExchangeRatesRepository,
    private val exchangeEngine: ExchangeEngine,
) {

    suspend fun execute(base: Currency, target: Currency, amount: Double): Result<ExchangeResult> {
        return combine(
            exchangeRatesRepository.getRates(),
            balanceRepository.getBalances(),
        ) { rates, balances ->
            rates to balances
        }
            .take(1)
            .map { (rates, balances) ->
                val buyeableCurrency = rates.first { rate -> rate.currency == target }
                val currentBalance = balances.first { balance -> balance.currency == base }

                ExchangeTransaction(
                    base = currentBalance.currency,
                    baseBalance = currentBalance.amount,
                    amount = amount,
                    rate = buyeableCurrency,
                )
            }
            .map { transaction ->
                exchangeEngine.convert(transaction)
            }
            .first()
    }
}
