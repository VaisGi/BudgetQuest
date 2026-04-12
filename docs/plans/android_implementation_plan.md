# BudgetQuest: Android & KMP Implementation Plan

## Current State (Phases 1-7 Completed)

The Android and Shared Kotlin Multiplatform (KMP) business logic are heavily mature.
- **KMP Core Business Logic:** Unified Repository pattern, Offline-First Persistence using SQLDelight (`SqlDelightUserRepository`, `SqlDelightTransactionRepository`). Reactive architecture utilizing Kotlin `Flow`.
- **Premium Compose UI:** Fully implemented custom Design System (`BQColor`, `BQTypography`, `BQShapes`). Deep gamification components like `BQGlassCard`, `BQProgressBar`, and `BQXPBadge` are integrated into all feature screens.
- **Type-Safe Navigation Shell:** Jetpack Compose navigation upgraded to strictly typed `@Serializable` destinations with a persistent Bottom Navigation Bar encapsulating the core features. SharedPreferences manage the first-launch Onboarding state.
- **Testing:** 9/9 robust unit tests covering data formatting and repository persistence running against `JdbcSqliteDriver`.

---

## Phase 8: Data Synchronization & Ktor Client (Next Steps)

### 1. Goal
Connect the Local SQLite database to the newly architected Backend (Supabase/PostgREST). Implement an offline-first synchronization strategy using Android's native scheduling tools.

### 2. Proposed Changes
- **Network Layer (`core:network`):** Implement `Ktor` HTTP client for cross-platform REST calls.
- **Background Sync Worker:** Implement Android's `WorkManager` to routinely push local changes and pull remote data from Supabase, even when the app is closed.
- **Conflict Resolution:** Implement "Last-Write-Wins" using `updated_at` timestamps on entities in tandem with the backend.

### 3. Verification Plan
- Unit test the Ktor Client implementations.
- **Flight-Mode Test:** Turn on airplane mode, create a transaction, disable airplane mode, and verify that `WorkManager` reliably executes the sync payload to the backend.

---

## Phase 10: Authentication & Login Module

### Prerequisites Before Implementation
1. **Gradle Networking & Client Dependencies:** Import `io.github.jan-tennert.supabase:gotrue-kt` alongside `io.ktor:ktor-client-core` across the KMP shared layers to allow native HTTP transactions with GoTrue.
2. **Gradle Security Dependencies:** Augment the `androidApp` module with `androidx.biometric:biometric` and `androidx.security:security-crypto`.
3. **Koin Dependency Injection:** Scaffold the `SharedModule` to reliably expose our `SupabaseClient` across all ViewModels securely.
4. **App Routing Architecture (`NavGraph.kt`):** Refactor the root navigation stack to safely intercept users between `Onboarding` and the `Dashboard`, nesting them into the `LoginScreen` pathway first.

### 1. Goal
Design and integrate a robust, multi-layered login experience utilizing our premium `BQGlassCard` styling. The authentication flow must support robust MFA (OTP) logic, distinct Email / Phone modes, and seamless Biometric bypass integration.

### 2. Proposed Changes
- **Login Compose UI:**
  - Build `LoginScreen.kt` using a deep interactive layout. Include a segmented toggle to switch between "Email" and "Mobile".
  - Build `OtpVerificationScreen.kt` featuring a spaced, secure 6-digit OTP entry field with auto-focus shifting.
- **Password Recovery Flow:**
  - Build `ForgotPasswordScreen.kt` for submitting the registered email/phone to receive the recovery OTP.
  - Build `ResetPasswordScreen.kt` for entering the new password once the recovery OTP is successfully validated against the GoTrue endpoint.
- **Biometric Prompt Framework:**
  - Integrate `androidx.biometric:biometric`.
  - Introduce `BiometricManager` in the Android `core:security` layer to display the system-native Fingerprint/FaceID prompt.
- **Secure Token Persistence:**
  - Leverage `EncryptedSharedPreferences` on Android to safely store the heavy `supabase.auth.jwt()` session tokens.
  - **The Biometric Flow:** When the user enables biometrics in Settings, the token is actively encrypted locally. Upon app restart, the Biometric Prompt requests hardware authorization. Once verified, the token is decrypted, directly injecting it into the Ktor Network client—completely bypassing the slow Email/Password/OTP wall.

### 3. Verification Plan
- Verify native Android Biometric Prompt trigger handling `SUCCESS` and `FAILURE` (Cancellation).
- Intercept the Supabase SMS/Email OTP flow on a physical device, verifying the deep link or manual code entry resolves the session to AAL2 correctly.
- Verify Password Recovery triggers the Supabase `/recover` email and successfully updates credentials.

---

## Phase 11: Unified Deep Blue Theme Migration

### 1. Goal
Complete a thorough overhaul of the core Design System (`core:designsystem`) across the entire KMP architecture to transition from the current "Electric Purple / Emerald Green" palette to a sleek, premium, and unified **Deep Blue (Facebook-esque) Palette**. This acts as the centralized source of truth so the UI is seamless everywhere.
Additionally, establish a unified routing orchestrator to safely handle **Deep Links and Universal Links** across the application ecosystem.

### 2. Proposed Changes
- **Unified Deep Linking:**
  - Create a centralized link interceptor in `MainActivity` (Android) to capture `android.intent.action.VIEW` intents.
  - Bind Deep Links natively using `navController.handleDeepLink(intent)`.
  - Ensure Supabase Magic Links (e.g., password reset callbacks) are securely caught and forwarded deep into the `ResetPasswordScreen.kt` pathway without crashing.
- **Color System (`BQColor.kt`):**
  - Deprecate Electric Purple metrics.
  - Inject new deep, trusting blues: `DeepBluePrimary` (e.g., `#1877F2` or darker `#0C4B9F`), `BlueAccent`, `DarkNavyBackground` (for dark mode).
- **Glassmorphism Updates (`BQGlassCard.kt`):**
  - Adjust the translucent overlay matrices to blend flawlessly atop the new Deep Blue background gradients so the aesthetic remains vibrant, not dull.
- **Typography & Components:**
  - Ensure all Buttons, Tab overlays, and Bottom Navigation Shell indicators switch to the trusted Blue accents.

### 3. Verification Plan
- Launch the main Dashboard and visually QA the color transition globally.
- Ensure the contrast ratios remain above WCAG accessibility standards across both Light and Dark mode.
