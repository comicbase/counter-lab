package com.counterlab.mvvm.ui

import androidx.lifecycle.ViewModel
import com.counterlab.mvvm.data.CounterRepository
import com.counterlab.mvvm.data.InMemoryCounterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CounterUiState(val count: Int = 0)

class CounterViewModel(
    private val repository: CounterRepository = InMemoryCounterRepository(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(CounterUiState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment() = _uiState.update { it.copy(count = repository.increment(it.count)) }
    fun decrement() = _uiState.update { it.copy(count = repository.decrement(it.count)) }
    fun reset() = _uiState.update { it.copy(count = repository.reset()) }
}
