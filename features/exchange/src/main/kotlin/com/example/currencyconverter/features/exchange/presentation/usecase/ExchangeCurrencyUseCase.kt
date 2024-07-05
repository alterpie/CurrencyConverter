package com.example.currencyconverter.features.exchange.presentation.usecase

import com.example.currencyconverter.core.balance.repository.BalanceRepository
import com.example.currencyconverter.core.exchange.converter.ExchangeEngine
import com.example.currencyconverter.core.exchange.converter.model.ExchangeResult
import com.example.currencyconverter.core.exchange.converter.model.ExchangeTransaction
import com.example.currencyconverter.core.exchange.rates.model.Currency
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import java.math.BigDecimal
import javax.inject.Inject

internal class ExchangeCurrencyUseCase @Inject constructor(
    private val balanceRepository: BalanceRepository,
    private val exchangeEngine: ExchangeEngine,
) {

    suspend fun execute(
        base: Currency,
        rate: ExchangeRate,
        amount: BigDecimal
    ): Result<ExchangeResult> {
        return balanceRepository.getBalances()
            .take(1)
            .map { balances ->
                val currentBalance = balances.first { balance -> balance.currency == base }

                val transaction = ExchangeTransaction(
                    base = currentBalance.currency,
                    baseBalance = currentBalance.amount,
                    amount = amount,
                    rate = rate,
                )

                exchangeEngine.convert(transaction)
                    .onSuccess { exchangeResult ->
                        balanceRepository.addToBalance(
                            currency = rate.currency,
                            amount = exchangeResult.convertedAmount,
                        )
                        balanceRepository.deductFromBalance(
                            currency = base,
                            amount = exchangeResult.tradedAmount + exchangeResult.fee,
                        )
                    }
            }
            .first()
    }
}
