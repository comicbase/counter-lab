package com.counterlab.mvi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.counterlab.mvi.ui.CounterScreen
import com.counterlab.mvi.ui.CounterViewModel
import com.counterlab.mvi.ui.theme.CounterLabMviTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CounterLabMviTheme {
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
