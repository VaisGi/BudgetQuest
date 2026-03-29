package com.budgetquest.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SavingsGoal(
    val id: String,
    val name: String,
    val emoji: String,
    val targetAmount: Double,
    val savedAmount: Double,
    val deadlineMonthYear: String? = null // "2026-12"
) {
    val progress: Float get() = if (targetAmount > 0) (savedAmount / targetAmount).toFloat().coerceIn(0f, 1f) else 0f
    val remainingAmount: Double get() = (targetAmount - savedAmount).coerceAtLeast(0.0)
    val isCompleted: Boolean get() = savedAmount >= targetAmount
    val milestone: SavingsMilestone get() = when {
        progress >= 1.0f -> SavingsMilestone.COMPLETE
        progress >= 0.75f -> SavingsMilestone.THREE_QUARTERS
        progress >= 0.50f -> SavingsMilestone.HALFWAY
        progress >= 0.25f -> SavingsMilestone.QUARTER
        else -> SavingsMilestone.STARTED
    }
}

@Serializable
enum class SavingsMilestone(val displayName: String, val badge: String) {
    STARTED("Just Started", "🌱"),
    QUARTER("25% There!", "🌿"),
    HALFWAY("Halfway!", "🌳"),
    THREE_QUARTERS("Almost There!", "🔥"),
    COMPLETE("Goal Reached!", "🏆")
}
