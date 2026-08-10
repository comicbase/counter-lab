package com.example.mydemo1mvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mydemo1mvi.ui.CounterScreen
import com.example.mydemo1mvi.ui.CounterViewModel
import com.example.mydemo1mvi.ui.theme.MyDemo1MviTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDemo1MviTheme {
                val viewModel: CounterViewModel = viewModel()
                val state by viewModel.state.collectAsStateWithLifecycle()
                CounterScreen(
                    state = state,
                    effects = viewModel.effects,
                    dispatch = viewModel::dispatch,
                )
            }
        }
    }
}
