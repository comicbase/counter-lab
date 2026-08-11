package com.counterlab.mvi

import com.counterlab.mvi.data.InMemoryCounterRepository
import com.counterlab.mvi.ui.CounterIntent
import com.counterlab.mvi.ui.CounterReducer
import com.counterlab.mvi.ui.CounterState
import org.junit.Assert.assertEquals
import org.junit.Test

class CounterReducerTest {
    private val reducer = CounterReducer(InMemoryCounterRepository())

    @Test
    fun intentsProducePredictableImmutableStates() {
        val incremented = reducer.reduce(CounterState(), CounterIntent.Increment)
        val decremented = reducer.reduce(incremented, CounterIntent.Decrement)
        val reset = reducer.reduce(CounterState(7), CounterIntent.Reset)
        assertEquals(CounterState(1), incremented)
        assertEquals(CounterState(0), decremented)
        assertEquals(CounterState(), reset)
    }
}
