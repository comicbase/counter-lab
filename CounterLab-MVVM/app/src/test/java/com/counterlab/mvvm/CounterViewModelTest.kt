package com.counterlab.mvvm

import com.counterlab.mvvm.ui.CounterViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CounterViewModelTest {
    @Test
    fun actionsUpdateExposedUiState() {
        val viewModel = CounterViewModel()
        viewModel.increment()
        viewModel.increment()
        viewModel.decrement()
        assertEquals(1, viewModel.uiState.value.count)
        viewModel.reset()
        assertEquals(0, viewModel.uiState.value.count)
    }
}
