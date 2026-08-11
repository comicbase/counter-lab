package com.counterlab.mvi.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun CounterScreen(
    state: CounterState,
    effects: Flow<CounterEffect>,
    dispatch: (CounterIntent) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                is CounterEffect.ShowMessage -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("MVI Counter", style = MaterialTheme.typography.headlineMedium)
            Text("${state.count}", style = MaterialTheme.typography.displayLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { dispatch(CounterIntent.Decrement) }) { Text("-1") }
                Button(onClick = { dispatch(CounterIntent.Increment) }) { Text("+1") }
            }
            TextButton(onClick = { dispatch(CounterIntent.Reset) }) { Text("Reset") }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CounterScreenPreview() {
    CounterScreen(CounterState(3), emptyFlow()) {}
}
