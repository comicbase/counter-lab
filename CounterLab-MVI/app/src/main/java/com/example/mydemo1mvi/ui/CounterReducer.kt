package com.example.mydemo1mvi.ui

import com.example.mydemo1mvi.data.CounterRepository

class CounterReducer(private val repository: CounterRepository) {
    fun reduce(state: CounterState, intent: CounterIntent): CounterState = when (intent) {
        CounterIntent.Increment -> state.copy(
            count = repository.applyDelta(state.count, 1),
        )
        CounterIntent.Decrement -> state.copy(
            count = repository.applyDelta(state.count, -1),
        )
        CounterIntent.Reset -> CounterState()
    }
}
