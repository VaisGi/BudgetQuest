# BudgetQuest: iOS Native Implementation Plan

## Current State

Currently, the iOS app (`iosApp`) is relying on the standard shared KMP architecture. 
- **Dependencies Setup:** The native dependency injection (`SharedModule`) is properly wired and exposing standard KMP Repositories and ViewModels.
- **SQLDelight:** The `NativeSqliteDriver` is initialized, giving iOS complete access to the unified offline persistence layer.
- **UI State:** The iOS views (SwiftUI) currently exist as scaffolding/wrappers but they lack the premium polished Gamification UI built for Android in Phase 5. They are functionally sound but visually barebones.

---

## Phase 9: iOS Premium UI Overhaul & Integration

### 1. Goal
Replicate the stunning visual design language of the Android application completely in pure, native SwiftUI, while strictly hooking into the shared Kotlin ViewModels without duplicating business logic.

### 2. Proposed Changes
- **SwiftUI Premium Tokens:** 
  - Create native Swift equivalents of `BQColor` gradients (Electric Purple, Emerald Green).
  - Implement a `BQGlassCard` modifier using SwiftUI's robust `ultraThinMaterial` and `blur` effects for authentic system-level glassmorphism.
- **State Integration:** 
  - Wrap the flow-based KMP `StateFlows` into native SwiftUI `@StateObject` / `ObservableObject` variables to trigger reactive iOS re-renders.
  - Connect iOS tap gestures natively triggering KMP ViewModel `onEvent()` intent systems.
- **Background Sync Workers:** 
  - Implement Apple's `BGTaskScheduler` to hook into the synchronization strategy devised during Phase 8 to ensure iOS data continually synchronizes automatically across iCloud boundaries / Supabase.

### 3. Verification Plan
- Launch Xcode and natively boot the iOS Simulator.
- Ensure transitions, animations, and the Bottom Tab Bar natively resemble the premium design specs without any jank.
- Fire an offline transaction on iOS and confirm it mirrors to Android via the synchronized Backend.
