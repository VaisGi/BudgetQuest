# BudgetQuest 🎮💰

A gamified budgeting & savings tracker built with **Kotlin Multiplatform (KMP)**.

## Architecture

**Config-Driven Design System** — ~70% shared code, 100% native UI.

```
shared/            → KMP module (domain, data, presentation)
androidApp/        → Jetpack Compose UI
iosApp/            → SwiftUI + Tuist
```

### Key Patterns
- **StateMachine<Config, Event>** — ViewModels produce immutable configs
- **Platforms are "dumb renderers"** — Compose/SwiftUI observe StateFlow, no logic in views
- **SKIE** — Kotlin Flow → Swift AsyncSequence for iOS interop
- **Tuist** — iOS project generated from `Project.swift`, no `.xcodeproj` in Git

## Getting Started

### Android
```bash
./gradlew :androidApp:installDebug
```

### iOS
```bash
cd iosApp
tuist generate
open BudgetQuest.xcworkspace
```

### Build Shared Framework
```bash
./gradlew :shared:assembleXCFramework
```

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Shared Logic | Kotlin Multiplatform |
| Android UI | Jetpack Compose + Material3 |
| iOS UI | SwiftUI |
| Networking | Ktor (ready for backend) |
| Persistence | SQLDelight (offline-first) |
| DI | Koin |
| iOS Interop | SKIE |
| iOS Project | Tuist |

## Monetization (RevenueCat-ready)
- **Free**: Basic categorization, 1 savings goal, streaks
- **Premium**: Unlimited goals, bank sync, AI insights, custom avatars

## Backend Integration
The data layer uses repository interfaces (`TransactionRepository`, `BudgetRepository`, etc.) backed by in-memory implementations. To integrate a backend:
1. Implement remote data sources using Ktor
2. Create `SyncedTransactionRepository` wrapping local + remote
3. Swap via Koin DI module — zero changes to ViewModels or UI
