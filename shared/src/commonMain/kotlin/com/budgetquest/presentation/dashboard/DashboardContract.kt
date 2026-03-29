package com.budgetquest.presentation.dashboard

import com.budgetquest.domain.model.BudgetStatus

/**
 * Immutable UI config for the Dashboard screen.
 * Platforms render this directly — no logic decisions in the view.
 */
data class DashboardConfig(
    val greeting: String = "Welcome back!",
    val streakDays: Int = 0,
    val streakBadgeVisible: Boolean = false,
    val xpProgress: Float = 0f,
    val currentLevel: Int = 1,
    val totalXp: Int = 0,
    val budgetCards: List<BudgetCardConfig> = emptyList(),
    val activeQuests: List<QuestCardConfig> = emptyList(),
    val savingsGoalName: String = "",
    val savingsGoalProgress: Float = 0f,
    val savingsGoalEmoji: String = "🎯",
    val hasSavingsGoal: Boolean = false,
    val isPremiumUser: Boolean = false,
    val showUpgradePrompt: Boolean = true,
    val isLoading: Boolean = true,
    val todaySpent: Double = 0.0,
    val monthlySpent: Double = 0.0,
    val monthlyBudgetTotal: Double = 0.0
)

data class BudgetCardConfig(
    val id: String,
    val categoryName: String,
    val categoryEmoji: String,
    val spent: Double,
    val limit: Double,
    val percentUsed: Float,
    val status: BudgetStatus
)

data class QuestCardConfig(
    val id: String,
    val title: String,
    val emoji: String,
    val progress: Float,
    val xpReward: Int,
    val isCompleted: Boolean
)

sealed class DashboardEvent {
    data object OnScreenLoaded : DashboardEvent()
    data class OnQuestTapped(val questId: String) : DashboardEvent()
    data object OnAddTransactionTapped : DashboardEvent()
    data object OnUpgradeTapped : DashboardEvent()
    data object OnViewAllBudgets : DashboardEvent()
    data object OnViewAllQuests : DashboardEvent()
    data object OnProfileTapped : DashboardEvent()
}
