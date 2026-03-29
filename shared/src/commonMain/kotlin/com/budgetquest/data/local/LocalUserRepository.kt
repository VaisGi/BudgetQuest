package com.budgetquest.data.local

import com.budgetquest.domain.model.Badge
import com.budgetquest.domain.model.Badges
import com.budgetquest.domain.model.UserProfile
import com.budgetquest.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class LocalUserRepository : UserRepository {
    private val profile = MutableStateFlow(
        UserProfile(
            id = "local_user",
            displayName = "Quester",
            joinedDate = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
        )
    )

    override fun getUserProfile(): Flow<UserProfile> = profile

    override suspend fun updateStreak(newStreak: Int) {
        profile.value = profile.value.copy(
            currentStreak = newStreak,
            longestStreak = maxOf(profile.value.longestStreak, newStreak)
        )
    }

    override suspend fun addXp(amount: Int) {
        val current = profile.value
        val newXp = current.totalXp + amount
        val newLevel = UserProfile.levelForXp(newXp)
        profile.value = current.copy(totalXp = newXp, level = newLevel)
    }

    override suspend fun unlockBadge(badge: Badge) {
        val current = profile.value
        if (current.badges.none { it.id == badge.id && it.isUnlocked }) {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            val updatedBadges = current.badges.toMutableList()
            val existingIndex = updatedBadges.indexOfFirst { it.id == badge.id }
            val unlockedBadge = badge.copy(unlockedAt = today)
            if (existingIndex >= 0) {
                updatedBadges[existingIndex] = unlockedBadge
            } else {
                updatedBadges.add(unlockedBadge)
            }
            profile.value = current.copy(badges = updatedBadges)
        }
    }

    override suspend fun setPremiumStatus(isPremium: Boolean) {
        profile.value = profile.value.copy(isPremium = isPremium)
    }

    override suspend fun updateAvatar(avatarId: String) {
        profile.value = profile.value.copy(avatarId = avatarId)
    }

    override suspend fun createDefaultProfile(name: String) {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
        profile.value = UserProfile(
            id = "local_user",
            displayName = name,
            joinedDate = today,
            badges = Badges.ALL.map { it.copy(unlockedAt = null) }
        )
    }
}
