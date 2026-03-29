package com.budgetquest.domain.repository

import com.budgetquest.domain.model.Badge
import com.budgetquest.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/**
 * User profile and gamification state repository.
 */
interface UserRepository {
    fun getUserProfile(): Flow<UserProfile>
    suspend fun updateStreak(newStreak: Int)
    suspend fun addXp(amount: Int)
    suspend fun unlockBadge(badge: Badge)
    suspend fun setPremiumStatus(isPremium: Boolean)
    suspend fun updateAvatar(avatarId: String)
    suspend fun createDefaultProfile(name: String)
}
