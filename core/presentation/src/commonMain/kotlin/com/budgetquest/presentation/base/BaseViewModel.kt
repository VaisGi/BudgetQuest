package com.budgetquest.presentation.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel that provides coroutine scope management.
 * Shared across platforms — Android uses lifecycle-aware collection,
 * iOS uses SKIE wrappers to observe StateFlow natively.
 */
abstract class BaseViewModel<Config, Event>(initialConfig: Config) : StateMachine<Config, Event> {
    protected val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val _state = MutableStateFlow(initialConfig)
    override val state: StateFlow<Config> = _state.asStateFlow()

    protected fun updateState(reducer: Config.() -> Config) {
        _state.value = _state.value.reducer()
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        scope.launch(block = block)
    }

    fun onCleared() {
        scope.cancel()
    }
}
