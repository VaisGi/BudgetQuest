---
name: "Config-Driven UI (KMP)"
description: "Guidelines for implementing the Config-Driven Design System pattern in Kotlin Multiplatform."
---

# Config-Driven Design System

BudgetQuest uses a unique **Config-Driven Design System** pattern where business logic and UI state are entirely decoupled from the rendering platform.

## Core Architecture
- **Platforms are Dumb Renderers**: Jetpack Compose (Android) and SwiftUI (iOS) exist *only* to render state and forward user intents.
- **Shared ViewModels**: All presentation logic lives in KMP `commonMain`.
- **Single Source of Truth**: ViewModels expose a single `StateFlow<Config>` and accept a single `onEvent(Event)` method.

## Implementation Steps for a New Screen

1. **Define the Contract** (`commonMain/presentation/<feature>/<Feature>Contract.kt`)
   - Create an immutable data class `Config` representing the exact UI state (texts, colors, visibilities).
   - Create a sealed class `Event` representing user interactions (taps, inputs).

2. **Implement the ViewModel** (`commonMain/presentation/<feature>/<Feature>ViewModel.kt`)
   - Extend `BaseViewModel` and implement `StateMachine<Config, Event>`.
   - Inject required `UseCases` via Koin.
   - Map domain data to the `Config` data class synchronously or via `combine()`.

3. **Android Render** (`androidApp/.../ui/screens/<Feature>Screen.kt`)
   - Call `val config by viewModel.state.collectAsStateWithLifecycle()`.
   - Map `Config` properties directly to Compose UI widgets.
   - Fire `viewModel.onEvent()` on click listeners.

4. **iOS Render** (`iosApp/Sources/<Feature>/<Feature>View.swift`)
   - Create a SKIE `ViewModelWrapper` bridging `StateFlow` to `@Published var config`.
   - Map `config` properties to SwiftUI Views.

## Anti-Patterns
- **🚫 NO logic in UI**: Never do date formatting, math, or if/else business rules in Compose/SwiftUI. That belongs in the shared ViewModel.
- **🚫 NO platform dependencies in shared**: Avoid expecting Android context or UIKit in the shared presentation layer.
