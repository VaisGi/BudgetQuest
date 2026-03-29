package com.budgetquest.presentation.base

import kotlinx.coroutines.flow.StateFlow

/**
 * Core contract for Config-Driven Design System.
 * ViewModels implement this to produce immutable UI configs from events.
 * Platforms (Compose/SwiftUI) observe the state and render — no logic on UI layer.
 */
interface StateMachine<Config, Event> {
    val state: StateFlow<Config>
    fun onEvent(event: Event)
}
