package com.example.currencyconverter.features.exchange.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.contentColorFor
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.primarySurface
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.currencyconverter.core.balance.model.CurrencyBalance
import com.example.currencyconverter.core.design_system.theme.AppTheme
import com.example.currencyconverter.core.design_system.theme.IndicationNegative
import com.example.currencyconverter.core.design_system.theme.IndicationPositive
import com.example.currencyconverter.core.design_system.theme.textPrimary
import com.example.currencyconverter.core.design_system.theme.textSecondary
import com.example.currencyconverter.features.exchange.R
import com.example.currencyconverter.features.exchange.presentation.ExchangeUiState
import kotlinx.collections.immutable.ImmutableList
import java.math.BigDecimal

@Composable
internal fun ExchangeScreen(
    state: ExchangeUiState,
    onSelectBalanceClick: () -> Unit,
    onExchangeAmountChange: (amountInput: String) -> Unit,
    onSelectRateClick: () -> Unit,
    onExchangeCurrencyClick: (amountInput: String) -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sellInput = remember {
        mutableStateOf("")
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .imePadding()
    ) {
        AppBar(onResetClick = onResetClick)
        if (state.ratesStatus === ExchangeUiState.RatesStatus.LOADING) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            return
        }
        BalancesSection(balances = state.balances)
        ExchangeSection(
            state = state,
            sellInput = sellInput,
            onExchangeAmountChange = onExchangeAmountChange,
            onSelectBalanceClick = onSelectBalanceClick,
            onSelectRateClick = onSelectRateClick,
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onExchangeCurrencyClick(sellInput.value) },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 78.dp)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            shape = RoundedCornerShape(50),
            enabled = state.selectedBalance != null && state.selectedRate != null
                    && state.exchangeAmount != null && state.exchangeAmount > BigDecimal.ZERO
                    && state.selectedBalance.currency != state.selectedRate.currency
        ) {
            Text(text = stringResource(R.string.submit))
        }
    }
}

@Composable
private fun AppBar(
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.background(MaterialTheme.colors.primarySurface)) {
        Spacer(
            modifier = Modifier
                .height(
                    WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding()
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.currency_converter),
                color = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.primarySurface)
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onResetClick) {
                Text(
                    text = stringResource(R.string.reset),
                    color = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.primarySurface)
                )
            }
        }
    }
}

@Composable
private fun BalancesSection(
    balances: ImmutableList<CurrencyBalance>,
) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.my_balances).uppercase(),
        modifier = Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colors.textSecondary,
    )
    Spacer(modifier = Modifier.height(18.dp))
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(balances) { balance ->
            BalanceItem(balance = balance)
            Spacer(modifier = Modifier.width(24.dp))
        }
    }
}

@Composable
private fun ExchangeSection(
    state: ExchangeUiState,
    sellInput: MutableState<String>,
    onExchangeAmountChange: (amountInput: String) -> Unit,
    onSelectBalanceClick: () -> Unit,
    onSelectRateClick: () -> Unit,
) {
    Spacer(modifier = Modifier.height(24.dp))
    if (state.ratesStatus === ExchangeUiState.RatesStatus.FAILURE) {
        Text(
            text = stringResource(R.string.failed_to_load_rates),
            color = MaterialTheme.colors.textPrimary,
            fontSize = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        return
    }
    Text(
        text = stringResource(R.string.currency_exchange).uppercase(),
        modifier = Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colors.textSecondary,
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(android.R.drawable.arrow_up_float),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(IndicationNegative),
            contentScale = ContentScale.None,
            colorFilter = ColorFilter.tint(Color.White),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.sell),
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colors.textPrimary,
        )
        TextField(
            value = sellInput.value,
            onValueChange = { input ->
                sellInput.value = input
                onExchangeAmountChange(input)
            },
            singleLine = true,
            modifier = Modifier.weight(0.5f),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onSelectBalanceClick)
                .minimumInteractiveComponentSize(),
        ) {
            Text(
                text = state.selectedBalance?.currency?.name ?: "",
                color = MaterialTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(android.R.drawable.arrow_down_float),
                contentDescription = null,
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Divider(modifier = Modifier.padding(horizontal = 16.dp))
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Image(
            painter = painterResource(android.R.drawable.arrow_down_float),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(IndicationPositive),
            contentScale = ContentScale.None,
            colorFilter = ColorFilter.tint(Color.White),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.receive),
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
            color = MaterialTheme.colors.textPrimary,
        )
        Text(text = state.exchangeAmount?.let { NumberFormatter.format(it) } ?: "")
        Spacer(modifier = Modifier.width(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onSelectRateClick)
                .minimumInteractiveComponentSize(),
        ) {
            Text(
                text = state.selectedRate?.currency?.name ?: "",
                color = MaterialTheme.colors.textPrimary,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(android.R.drawable.arrow_down_float),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun BalanceItem(
    balance: CurrencyBalance,
    modifier: Modifier = Modifier,
) {
    val balanceFormated = NumberFormatter.format(balance.amount)
    Text(
        text = "$balanceFormated ${balance.currency.name}",
        modifier = modifier,
        color = MaterialTheme.colors.textPrimary,
        fontWeight = FontWeight.Bold,
    )
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
            onResetClick = {},
        )
    }
}
