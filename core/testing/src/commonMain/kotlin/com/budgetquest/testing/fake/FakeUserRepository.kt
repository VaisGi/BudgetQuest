package com.budgetquest.testing.fake

import com.budgetquest.domain.model.Badge
import com.budgetquest.domain.model.UserProfile
import com.budgetquest.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserRepository : UserRepository {
    private val userProfileFlow = MutableStateFlow(UserProfile(
        id = "test_user_id",
        displayName = "Test User",
        totalXp = 0,
        level = 1,
        currentStreak = 0,
        longestStreak = 0,
        isPremium = false,
        badges = emptyList(),
        joinedDate = "2023-01-01"
    ))

    fun populateProfile(profile: UserProfile) {
        userProfileFlow.value = profile
    }

    override fun getUserProfile(): Flow<UserProfile> = userProfileFlow

    override suspend fun updateStreak(newStreak: Int) {
        val current = userProfileFlow.value
        val newLongest = maxOf(current.longestStreak, newStreak)
        userProfileFlow.value = current.copy(currentStreak = newStreak, longestStreak = newLongest)
    }

    override suspend fun addXp(amount: Int) {
        val current = userProfileFlow.value
        val newXp = current.totalXp + amount
        val newLevel = UserProfile.levelForXp(newXp)
        userProfileFlow.value = current.copy(totalXp = newXp, level = newLevel)
    }

    override suspend fun unlockBadge(badge: Badge) {
        val current = userProfileFlow.value
        if (current.badges.none { it.id == badge.id }) {
            userProfileFlow.value = current.copy(
                badges = current.badges + badge.copy(unlockedAt = "today")
            )
        }
    }

    override suspend fun setPremiumStatus(isPremium: Boolean) {
        userProfileFlow.value = userProfileFlow.value.copy(isPremium = isPremium)
    }

    override suspend fun updateAvatar(avatarId: String) {
        userProfileFlow.value = userProfileFlow.value.copy(avatarId = avatarId)
    }

    override suspend fun createDefaultProfile(name: String) {
        userProfileFlow.value = userProfileFlow.value.copy(displayName = name, joinedDate = "now")
    }
}
