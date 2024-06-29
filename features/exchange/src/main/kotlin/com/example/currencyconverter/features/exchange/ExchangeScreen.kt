package com.example.currencyconverter.features.exchange

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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

@Composable
fun ExchangeScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
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
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            shape = RoundedCornerShape(50)
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
        ExchangeScreen()
    }
}
