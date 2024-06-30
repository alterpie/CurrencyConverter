package com.example.currencyconverter.features.exchange.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.core.balance.model.CurrencyBalance
import kotlinx.collections.immutable.ImmutableList
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectBalanceContent(
    balances: ImmutableList<CurrencyBalance>,
    sheetState: SheetState,
    showBottomSheet: Boolean,
    onBalanceClick: (CurrencyBalance) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss,
            modifier = modifier,
            containerColor = MaterialTheme.colors.surface,
        ) {
            Content(
                balances = balances,
                onBalanceClick = onBalanceClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun Content(
    balances: ImmutableList<CurrencyBalance>,
    modifier: Modifier = Modifier,
    onBalanceClick: (CurrencyBalance) -> Unit,
) {
    Column(modifier = modifier) {
        LazyColumn {
            items(balances) { balance ->
                CurrencyBalanceItem(
                    balance = balance,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { onBalanceClick(balance) })
                )
                Divider()
            }
        }
    }
}

@Composable
private fun CurrencyBalanceItem(balance: CurrencyBalance, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = balance.currency.name, modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
        )
        Text(text = String.format(Locale.getDefault(), "%.2f", balance.amount))
    }
}
