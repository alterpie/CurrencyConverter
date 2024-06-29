package com.example.currencyconverter.features.exchange.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.features.exchange.presentation.ExchangeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeDestination() {
    val viewModel = hiltViewModel<ExchangeViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(viewModel)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(viewModel)
        }
    }
    val state by viewModel.state.collectAsState()

    ExchangeScreen()

    val coroutineScope = rememberCoroutineScope()

    val selectRateSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSelectRateSheet by remember { mutableStateOf(false) }
    SelectRateContent(
        rates = state.rates,
        sheetState = selectRateSheetState,
        showBottomSheet = showSelectRateSheet,
        onRateClick = {
            coroutineScope.launch {
                selectRateSheetState.hide()
            }.invokeOnCompletion {
                if (!selectRateSheetState.isVisible) {
                    showSelectRateSheet = false
                }
            }
        },
        onDismiss = { showSelectRateSheet = false }
    )
}
