package com.example.mydemo1mvi.ui

data class CounterState(val count: Int = 0)

sealed interface CounterIntent {
    data object Increment : CounterIntent
    data object Decrement : CounterIntent
    data object Reset : CounterIntent
}

sealed interface CounterEffect {
    data class ShowMessage(val message: String) : CounterEffect
}
