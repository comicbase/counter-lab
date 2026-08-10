package com.example.mydemo1mvi.data

interface CounterRepository {
    fun applyDelta(value: Int, delta: Int): Int
}

class InMemoryCounterRepository : CounterRepository {
    override fun applyDelta(value: Int, delta: Int) = value + delta
}
