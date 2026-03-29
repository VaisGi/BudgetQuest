package com.budgetquest.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Quest(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val xpReward: Int,
    val type: QuestType,
    val targetValue: Double,
    val currentValue: Double = 0.0,
    val isCompleted: Boolean = false,
    val expiresAt: String? = null // ISO date
) {
    val progress: Float get() = if (targetValue > 0) (currentValue / targetValue).toFloat().coerceIn(0f, 1f) else 0f
}

@Serializable
enum class QuestType(val displayName: String) {
    SPENDING_LIMIT("Spending Limit"),
    SAVINGS_DEPOSIT("Savings Deposit"),
    NO_SPEND_CATEGORY("No-Spend Challenge"),
    STREAK_TARGET("Streak Target"),
    BUDGET_COMPLIANCE("Stay Under Budget"),
    TRANSACTION_COUNT("Log Transactions")
}

object QuestTemplates {
    fun weeklyQuests(): List<Quest> = listOf(
        Quest(
            id = "quest_no_coffee",
            title = "No Coffee Week",
            description = "Skip coffee purchases for 7 days and save ~$30",
            emoji = "☕",
            xpReward = 150,
            type = QuestType.NO_SPEND_CATEGORY,
            targetValue = 7.0
        ),
        Quest(
            id = "quest_pack_lunch",
            title = "Pack Lunch Pro",
            description = "Pack your own lunch 5 days this week",
            emoji = "🥪",
            xpReward = 100,
            type = QuestType.SPENDING_LIMIT,
            targetValue = 5.0
        ),
        Quest(
            id = "quest_log_daily",
            title = "Daily Logger",
            description = "Log at least 1 transaction every day this week",
            emoji = "📊",
            xpReward = 75,
            type = QuestType.TRANSACTION_COUNT,
            targetValue = 7.0
        ),
        Quest(
            id = "quest_save_50",
            title = "Fifty Saver",
            description = "Put $50 towards any savings goal",
            emoji = "🎯",
            xpReward = 120,
            type = QuestType.SAVINGS_DEPOSIT,
            targetValue = 50.0
        ),
        Quest(
            id = "quest_under_budget",
            title = "Budget Keeper",
            description = "Stay under budget in all categories for 3 days",
            emoji = "🛡️",
            xpReward = 200,
            type = QuestType.BUDGET_COMPLIANCE,
            targetValue = 3.0
        )
    )
}
