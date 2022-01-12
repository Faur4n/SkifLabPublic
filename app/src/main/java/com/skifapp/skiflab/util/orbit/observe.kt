package com.skifapp.skiflab.util.orbit
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

/**
 * Observe [Container.sideEffectFlow] in a Compose [LaunchedEffect].
 */
@SuppressLint("ComposableNaming")
@Composable
public fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.collectSideEffect(
    sideEffect: (suspend (sideEffect: SIDE_EFFECT) -> Unit)
) {
    LaunchedEffect(this) {
        launch {
            container.sideEffectFlow.collect { sideEffect(it) }
        }
    }
}

/**
 * Observe [Container.stateFlow] in a Compose [LaunchedEffect].
 */
@SuppressLint("ComposableNaming")
@Composable
public fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.collectState(
    sideEffect: (suspend (state: STATE) -> Unit)
) {
    LaunchedEffect(this) {
        launch {
            container.stateFlow.collect { sideEffect(it) }
        }
    }
}

/**
 * Observe [Container.stateFlow] as [State].
 */
@Composable
public fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.collectAsState(): State<STATE> =
    container.stateFlow.collectAsState()