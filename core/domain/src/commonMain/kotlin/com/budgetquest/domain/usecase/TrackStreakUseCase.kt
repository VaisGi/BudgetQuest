package com.budgetquest.domain.usecase

import com.budgetquest.domain.model.Badges
import com.budgetquest.domain.model.UserProfile
import com.budgetquest.domain.repository.UserRepository
import kotlinx.coroutines.flow.first

class TrackStreakUseCase(
    private val userRepo: UserRepository
) {
    companion object {
        const val XP_DAILY_STREAK = 5
        const val XP_STREAK_BONUS_7 = 50
        const val XP_STREAK_BONUS_30 = 200
    }

    /**
     * Called when user logs a transaction for the day.
     * Increments streak, awards XP, and checks for streak badges.
     */
    suspend fun recordDailyActivity() {
        val profile = userRepo.getUserProfile().first()
        val newStreak = profile.currentStreak + 1

        userRepo.updateStreak(newStreak)
        userRepo.addXp(XP_DAILY_STREAK)

        // Streak milestone badges and bonus XP
        if (newStreak == 7) {
            userRepo.unlockBadge(Badges.STREAK_7)
            userRepo.addXp(XP_STREAK_BONUS_7)
        }
        if (newStreak == 30) {
            userRepo.unlockBadge(Badges.STREAK_30)
            userRepo.addXp(XP_STREAK_BONUS_30)
        }

        // Level milestone badges
        val updatedProfile = userRepo.getUserProfile().first()
        val level = UserProfile.levelForXp(updatedProfile.totalXp)
        if (level >= 5) userRepo.unlockBadge(Badges.LEVEL_5)
        if (level >= 10) userRepo.unlockBadge(Badges.LEVEL_10)
    }

    /**
     * Called on app launch to check if streak should reset (no activity yesterday).
     */
    suspend fun checkStreakContinuity(lastActivityDate: String, todayDate: String) {
        if (lastActivityDate != todayDate) {
            // If the gap is more than 1 day, reset streak
            // Simple check — can be enhanced with proper date math
            val profile = userRepo.getUserProfile().first()
            if (profile.currentStreak > 0) {
                // For now, we keep streak. Reset logic should compare dates properly.
                // This is a placeholder for proper date comparison when kotlinx-datetime is wired.
            }
        }
    }
}
