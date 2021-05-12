package com.github.omarmiatello.sourceoftruth.appsample

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class Steps(
    private vararg val steps: Pair<() -> Unit, String>,
    private val scope: CoroutineScope,
) {
    private val currentStep = MutableStateFlow(1)
    val stepStrings = currentStep.map { steps.take(it).map { it.second } }
        .stateIn(scope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        scope.launch {
            currentStep.collect { n -> steps.take(n).forEach { it.first() } }
        }
    }

    fun prev() {
        currentStep.value = (currentStep.value - 1).coerceAtLeast(1)
    }

    fun next() {
        currentStep.value = (currentStep.value + 1).coerceAtMost(steps.size)
    }

    @Composable
    fun Buttons() {
        Row(
            modifier = Modifier
                .padding(16.dp),
        ) {
            Button(onClick = { prev() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                )
                Text("Prev")
            }
            Text(
                text = "${currentStep.collectAsState().value}",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterVertically),
            )
            Button(onClick = { next() }) {
                Text("Next")
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                )
            }
        }
    }
}
