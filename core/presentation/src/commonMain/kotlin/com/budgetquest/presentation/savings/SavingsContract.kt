package com.budgetquest.presentation.savings

import com.budgetquest.domain.model.SavingsMilestone

data class SavingsConfig(
    val goals: List<SavingsGoalItemConfig> = emptyList(),
    val isLoading: Boolean = true,
    val showAddGoal: Boolean = false,
    val newGoalName: String = "",
    val newGoalEmoji: String = "🎯",
    val newGoalTarget: String = "",
    val isPremiumUser: Boolean = false,
    val canAddMoreGoals: Boolean = true, // Free tier: 1 goal max
    val showPremiumGate: Boolean = false,
    val showMilestoneAnimation: Boolean = false,
    val reachedMilestone: SavingsMilestone? = null
)

data class SavingsGoalItemConfig(
    val id: String,
    val name: String,
    val emoji: String,
    val savedAmount: Double,
    val targetAmount: Double,
    val progress: Float,
    val milestone: SavingsMilestone,
    val isCompleted: Boolean
)

sealed class SavingsEvent {
    data object OnScreenLoaded : SavingsEvent()
    data object OnAddGoalTapped : SavingsEvent()
    data class OnGoalNameChanged(val name: String) : SavingsEvent()
    data class OnGoalEmojiChanged(val emoji: String) : SavingsEvent()
    data class OnGoalTargetChanged(val target: String) : SavingsEvent()
    data object OnSaveGoal : SavingsEvent()
    data object OnDismissAddGoal : SavingsEvent()
    data class OnAddSavings(val goalId: String, val amount: Double) : SavingsEvent()
    data class OnDeleteGoal(val goalId: String) : SavingsEvent()
    data object OnDismissMilestone : SavingsEvent()
}
