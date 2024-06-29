package com.example.currencyconverter.features.exchange.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.currencyconverter.core.design_system.theme.AppTheme
import com.example.currencyconverter.features.exchange.presentation.ExchangeUiState

@Composable
internal fun ExchangeScreen(
    state: ExchangeUiState,
    onSelectBalanceClick: () -> Unit,
    onExchangeAmountChange: (amountInput: String) -> Unit,
    onSelectRateClick: () -> Unit,
    onExchangeCurrencyClick: (amountInput: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.screenStatus === ExchangeUiState.ScreenStatus.LOADING) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        return
    }
    var sellInput by remember {
        mutableStateOf("")
    }
    Column(modifier = modifier.fillMaxSize()) {
        AppBar()
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "My balances")
        Spacer(modifier = Modifier.height(18.dp))
        // balances
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Currency exchange")
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(android.R.drawable.arrow_up_float),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Red),
                contentScale = ContentScale.None,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sell", modifier = Modifier.weight(1f))
            TextField(
                value = sellInput,
                onValueChange = { input ->
                    sellInput = input
                    onExchangeAmountChange(input)
                },
                singleLine = true,
                modifier = Modifier.weight(0.5f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onSelectBalanceClick)
            ) {
                Text(text = state.selectedBalance?.currency?.name ?: "")
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(android.R.drawable.arrow_down_float),
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(android.R.drawable.arrow_down_float),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Green),
                contentScale = ContentScale.None,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Receive",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            )
            Text(text = state.exchangeAmount?.let { String.format("%.2f", it) } ?: "")
            Spacer(modifier = Modifier.width(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onSelectRateClick)
            ) {
                Text(text = state.selectedRate?.currency?.name ?: "")
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(android.R.drawable.arrow_down_float),
                    contentDescription = null,
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onExchangeCurrencyClick(sellInput) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            shape = RoundedCornerShape(50),
            enabled = state.selectedBalance != null && state.selectedRate != null
                    && state.exchangeAmount != null && state.exchangeAmount > 0
        ) {
            Text(text = "Submit")
        }
    }
}

@Composable
private fun AppBar(modifier: Modifier = Modifier) {
    TopAppBar(modifier = modifier) {
        Text(text = "Currency converter")
    }
}

@PreviewLightDark
@Composable
private fun PreviewExchangeScreen() {
    AppTheme {
        ExchangeScreen(
            state = ExchangeUiState(),
            onExchangeCurrencyClick = {},
            onSelectBalanceClick = {},
            onSelectRateClick = {},
            onExchangeAmountChange = {},
        )
    }
}
