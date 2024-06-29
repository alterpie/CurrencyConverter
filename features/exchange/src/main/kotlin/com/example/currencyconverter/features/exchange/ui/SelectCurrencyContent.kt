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
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.core.exchange.rates.model.ExchangeRate
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectRateContent(
    rates: ImmutableList<ExchangeRate>,
    sheetState: SheetState,
    showBottomSheet: Boolean,
    onRateClick: (ExchangeRate) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss,
            modifier = modifier
        ) {
            Content(rates = rates, onRateClick = onRateClick, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun Content(
    rates: ImmutableList<ExchangeRate>,
    modifier: Modifier = Modifier,
    onRateClick: (ExchangeRate) -> Unit,
) {
    Column(modifier = modifier) {
        LazyColumn {
            items(rates) { rate ->
                ExchangeRateItem(
                    rate = rate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { onRateClick(rate) })
                )
                Divider()
            }
        }
    }
}

@Composable
private fun ExchangeRateItem(rate: ExchangeRate, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rate.currency.name, modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
        )
        Text(text = String.format("%.2f", rate.value))
    }
}
