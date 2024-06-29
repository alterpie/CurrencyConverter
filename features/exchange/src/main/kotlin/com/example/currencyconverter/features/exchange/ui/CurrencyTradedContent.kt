package com.example.currencyconverter.features.exchange.ui

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.currencyconverter.features.exchange.R
import com.example.currencyconverter.features.exchange.presentation.ExchangeUiState
import java.text.DecimalFormat

@Composable
internal fun CurrencyExchangeStatusContent(
    status: ExchangeUiState.ExchangeStatus,
    onStatusHandled: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (status) {
        ExchangeUiState.ExchangeStatus.ErrorFeeTooHigh -> {
            AlertDialog(
                modifier = modifier,
                title = { Text(text = stringResource(R.string.exchange_conversion_error_title)) },
                text = {
                    Text(text = stringResource(R.string.exchange_conversion_error_message))
                },
                onDismissRequest = onStatusHandled,
                confirmButton = {
                    Button(onClick = onStatusHandled) {
                        Text(text = stringResource(R.string.exchange_conversion_error_action))
                    }
                },
            )
        }

        ExchangeUiState.ExchangeStatus.Idle -> Unit
        ExchangeUiState.ExchangeStatus.Loading -> Unit
        is ExchangeUiState.ExchangeStatus.Success -> {
            val format = DecimalFormat.getInstance().apply { maximumFractionDigits = 2 }
            val traded = format.format(status.traded.second)
            val bought = format.format(status.bought.second)
            val fee = status.fee?.second?.let(format::format)
            AlertDialog(
                title = { Text(text = stringResource(R.string.exchange_successful_trade_title)) },
                text = {
                    val value = if (fee != null) {
                        stringResource(
                            R.string.exchange_successful_trade_message_with_fee,
                            R.string.exchange_successful_trade_message,
                            traded,
                            status.traded.first.name,
                            bought,
                            status.bought.first.name,
                            fee,
                            status.fee.first.name,
                        )
                    } else {
                        stringResource(
                            R.string.exchange_successful_trade_message,
                            traded,
                            status.traded.first.name,
                            bought,
                            status.bought.first.name,
                        )
                    }
                    Text(text = value)
                },
                modifier = modifier,
                onDismissRequest = onStatusHandled,
                confirmButton = {
                    Button(onClick = onStatusHandled) {
                        Text(text = stringResource(R.string.exchange_successful_trade_action))
                    }
                },
            )
        }
    }
}
