# BudgetQuest: Task Tracker

## Phase 10: Android Authentication & Login Module

**Prerequisites**
- [x] Add Supabase GoTrue & Ktor network dependencies into `gradle/libs.versions.toml`
- [x] Implement Koin DI for `SupabaseClient` in `SharedModule.kt`
- [x] Add `androidx.biometric:biometric` and `security-crypto` to `androidApp/build.gradle.kts`
- [x] Refactor Navigation stack (`NavGraph.kt` & `MainAppShell`) to funnel via `LoginScreen`

**UI & Framework**
- [x] Implement `BiometricManager` wrapper in Android
- [x] Implement `AuthViewModel` utilizing GoTrue Kotlin bindings
- [x] Build Premium `LoginScreen.kt` using `BQGlassCard`
- [x] Build `OtpVerificationScreen.kt`
- [x] Intercept Android hardware keystore and auto-unlock Supabase session Tokens
