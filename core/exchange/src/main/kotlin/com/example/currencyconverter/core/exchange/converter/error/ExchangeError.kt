package com.example.currencyconverter.core.exchange.converter.error

sealed class ExchangeError : Throwable() {

    class FeeTooHigh : ExchangeError()
    class NotEnoughBalance : ExchangeError()
}
