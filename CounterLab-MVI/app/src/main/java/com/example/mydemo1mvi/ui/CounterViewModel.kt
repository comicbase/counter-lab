package com.example.mydemo1mvi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydemo1mvi.data.InMemoryCounterRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CounterViewModel : ViewModel() {
    private val reducer = CounterReducer(InMemoryCounterRepository())
    private val _state = MutableStateFlow(CounterState())
    val state: StateFlow<CounterState> = _state.asStateFlow()

    private val effectChannel = Channel<CounterEffect>(Channel.BUFFERED)
    val effects = effectChannel.receiveAsFlow()

    fun dispatch(intent: CounterIntent) {
        _state.update { reducer.reduce(it, intent) }
        if (intent == CounterIntent.Reset) {
            viewModelScope.launch {
                effectChannel.send(CounterEffect.ShowMessage("Counter reset"))
            }
        }
    }
}
