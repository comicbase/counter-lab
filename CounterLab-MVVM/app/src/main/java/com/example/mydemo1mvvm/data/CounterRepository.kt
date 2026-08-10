package com.example.mydemo1mvvm.data

interface CounterRepository {
    fun increment(value: Int): Int
    fun decrement(value: Int): Int
    fun reset(): Int
}

class InMemoryCounterRepository : CounterRepository {
    override fun increment(value: Int) = value + 1
    override fun decrement(value: Int) = value - 1
    override fun reset() = 0
}
