package com.example.currencyconverter.features.exchange

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ExchangeScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        AppBar()
        Text(text = "My balances")
        // balances
        Text(text = "Currency exchange")
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Red)
            ) {
                Image(
                    painter = painterResource(android.R.drawable.arrow_up_float),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sell", modifier = Modifier.weight(1f))
            var sellInput by remember {
                mutableStateOf("")
            }
            TextField(
                value = sellInput,
                onValueChange = { sellInput = it },
                singleLine = true,
                modifier = Modifier.weight(0.5f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "EUR")
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(android.R.drawable.arrow_down_float),
                contentDescription = null
            )
        }
        Divider()
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Green)
            ) {
                Image(
                    painter = painterResource(android.R.drawable.arrow_down_float),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Receive", modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            )
            Text(text = "")
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "USD")
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(android.R.drawable.arrow_down_float),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun AppBar(modifier: Modifier = Modifier) {
    TopAppBar(modifier = modifier) {
        Text(text = "Currency converter")
    }
}

@Preview
@Composable
private fun PreviewExchangeScreen() {
    ExchangeScreen()
}
