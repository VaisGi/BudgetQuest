# BudgetQuest — Client-Side Development Tasks

## Phase 1: Monorepo Initialization & Tooling
- [x] Create root Gradle config (`settings.gradle.kts`, `build.gradle.kts`)
- [x] Create `gradle/libs.versions.toml` dependency catalog
- [x] Create `gradle.properties` with KMP flags
- [x] Create `.gitignore` (exclude xcodeproj, build dirs)
- [ ] Initialize Gradle wrapper (requires JDK on machine)

## Phase 2: KMP Shared Module — Domain Layer
- [x] Create `shared/build.gradle.kts` with KMP targets
- [x] Create domain models: `Transaction`, `Budget`, `SavingsGoal`, `UserProfile`, `Quest`
- [x] Create repository interfaces (backend-ready abstractions)
- [x] Create use cases: `AddTransaction`, `CalculateBudget`, `TrackStreak`, `GetQuests`

## Phase 3: KMP Shared Module — Data Layer (Local-Only)
- [x] Set up SQLDelight schema for offline persistence
- [x] Implement local-only repository implementations (in-memory with StateFlow)
- [x] Create mock/stub remote data source interfaces (for future backend)
- [x] Implement offline-first data flow pattern

## Phase 4: KMP Shared Module — Presentation Layer
- [x] Create `StateMachine` interface and `BaseViewModel` base class
- [x] Create `DashboardViewModel` + `DashboardConfig`/`DashboardEvent`
- [x] Create `TransactionViewModel` + contract
- [x] Create `BudgetViewModel` + contract
- [x] Create `SavingsGoalViewModel` + contract (with premium gating)
- [x] Create `OnboardingViewModel` + contract
- [ ] Create `PaywallViewModel` + contract (RevenueCat-ready) — deferred to monetization phase

## Phase 5: Android App — Jetpack Compose
- [x] Create `androidApp/build.gradle.kts`
- [x] Set up BudgetQuest theme (Material3, gamified palette)
- [x] Create `DashboardScreen` composable
- [x] Create `TransactionScreen` composable
- [x] Create `BudgetScreen` composable
- [x] Create `SavingsGoalScreen` composable (with milestone celebrations)
- [x] Create `QuestScreen` composable
- [x] Create `OnboardingScreen` composable (4-step flow)
- [x] Set up Compose Navigation graph
- [x] Set up Koin DI module

## Phase 6: iOS App — SwiftUI + Tuist
- [x] Create Tuist `Project.swift` manifest
- [x] Create SwiftUI app entry point (`BudgetQuestApp.swift`)
- [x] Create SKIE ViewModel wrapper documentation/patterns
- [x] Create `DashboardView` (with placeholder data matching KMP config)
- [x] Create `TransactionView`
- [x] Create `BudgetView`
- [x] Create `SavingsView`
- [x] Create `QuestView`
- [x] Create `OnboardingView` (4-step flow)
- [x] Create SwiftUI theme/design tokens (`BQTheme.swift`)
- [x] Create `MainTabView` with tab-based navigation

## Phase 7: Gamification Engine (Shared)
- [x] Implement streak tracking logic (`TrackStreakUseCase`)
- [x] Implement XP/leveling system (`UserProfile` + `addXp`)
- [x] Implement quest completion engine (`GetQuestsUseCase`)
- [x] Implement achievement badge system (`Badges` object + unlock logic)

## Phase 8: Verification
- [ ] Write `commonTest` unit tests for use cases
- [ ] Write `commonTest` unit tests for ViewModels
- [ ] Verify Android build compiles (requires Gradle wrapper + JDK)
- [ ] Verify iOS project generates via Tuist
