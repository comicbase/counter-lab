package com.example.mydemo1mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mydemo1mvvm.ui.CounterScreen
import com.example.mydemo1mvvm.ui.CounterViewModel
import com.example.mydemo1mvvm.ui.theme.MyDemo1MvvmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDemo1MvvmTheme {
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
