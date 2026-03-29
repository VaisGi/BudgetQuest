package com.budgetquest.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val displayName: String,
    val avatarId: String = "default_avatar",
    val level: Int = 1,
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val isPremium: Boolean = false,
    val badges: List<Badge> = emptyList(),
    val joinedDate: String = "" // ISO date
) {
    val xpForCurrentLevel: Int get() = xpThresholdForLevel(level)
    val xpForNextLevel: Int get() = xpThresholdForLevel(level + 1)
    val xpProgressInLevel: Float get() {
        val needed = xpForNextLevel - xpForCurrentLevel
        val earned = totalXp - xpForCurrentLevel
        return if (needed > 0) (earned.toFloat() / needed).coerceIn(0f, 1f) else 1f
    }

    companion object {
        fun xpThresholdForLevel(level: Int): Int = when {
            level <= 1 -> 0
            level == 2 -> 500
            level == 3 -> 1500
            level == 4 -> 3000
            level == 5 -> 5000
            level == 6 -> 8000
            level == 7 -> 12000
            level == 8 -> 17000
            level == 9 -> 23000
            level >= 10 -> 30000
            else -> 0
        }

        fun levelForXp(xp: Int): Int {
            for (lvl in 10 downTo 1) {
                if (xp >= xpThresholdForLevel(lvl)) return lvl
            }
            return 1
        }
    }
}

@Serializable
data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val emoji: String,
    val unlockedAt: String? = null // ISO date or null if locked
) {
    val isUnlocked: Boolean get() = unlockedAt != null
}

object Badges {
    val FIRST_BUDGET = Badge("first_budget", "First Budget", "Created your first budget", "🏅")
    val FIRST_TRANSACTION = Badge("first_transaction", "Tracker", "Logged your first expense", "📝")
    val STREAK_7 = Badge("streak_7", "Week Warrior", "7-day logging streak", "🔥")
    val STREAK_30 = Badge("streak_30", "Monthly Master", "30-day logging streak", "⚡")
    val UNDER_BUDGET = Badge("under_budget", "Budget Boss", "Stayed under budget for a month", "👑")
    val SAVINGS_FIRST = Badge("savings_first", "Saver", "Made your first savings deposit", "💰")
    val SAVINGS_COMPLETE = Badge("savings_complete", "Goal Crusher", "Completed a savings goal", "🏆")
    val QUEST_COMPLETE = Badge("quest_complete", "Quester", "Completed your first quest", "⚔️")
    val LEVEL_5 = Badge("level_5", "Rising Star", "Reached Level 5", "⭐")
    val LEVEL_10 = Badge("level_10", "Finance Legend", "Reached Level 10", "🌟")

    val ALL = listOf(
        FIRST_BUDGET, FIRST_TRANSACTION, STREAK_7, STREAK_30,
        UNDER_BUDGET, SAVINGS_FIRST, SAVINGS_COMPLETE,
        QUEST_COMPLETE, LEVEL_5, LEVEL_10
    )
}
