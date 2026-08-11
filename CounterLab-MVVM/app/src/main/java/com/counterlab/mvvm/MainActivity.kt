package com.counterlab.mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.counterlab.mvvm.ui.CounterScreen
import com.counterlab.mvvm.ui.CounterViewModel
import com.counterlab.mvvm.ui.theme.CounterLabMvvmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CounterLabMvvmTheme {
                val viewModel: CounterViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                CounterScreen(
                    uiState = uiState,
                    onIncrement = viewModel::increment,
                    onDecrement = viewModel::decrement,
                    onReset = viewModel::reset,
                )
            }
        }
    }
}
