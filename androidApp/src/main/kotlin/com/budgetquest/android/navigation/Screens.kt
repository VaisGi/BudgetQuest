package com.budgetquest.android.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for Compose Navigation (>= 2.8.0).
 * By using classes/objects annotated with @Serializable, we achieve compile-time safety.
 */

@Serializable
data object Onboarding

@Serializable
data object Dashboard

@Serializable
data object AddTransaction

@Serializable
data object Budgets

@Serializable
data object Savings

@Serializable
data object Quests
