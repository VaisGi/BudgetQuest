package com.budgetquest.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.budgetquest.db.BudgetQuestDatabaseQueries
import com.budgetquest.domain.model.Badge
import com.budgetquest.domain.model.Badges
import com.budgetquest.domain.model.UserProfile
import com.budgetquest.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

private const val LOCAL_USER_ID = "local_user"

class SqlDelightUserRepository(
    private val queries: BudgetQuestDatabaseQueries
) : UserRepository {

    init {
        // Seed a default profile on first use so mutation methods can always find a row
        ensureProfileExists()
    }

    override fun getUserProfile(): Flow<UserProfile> =
        queries.getUserProfile(LOCAL_USER_ID)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { entity -> entity?.toDomain() ?: defaultProfile() }

    override suspend fun updateStreak(newStreak: Int) {
        val current = queries.getUserProfile(LOCAL_USER_ID).executeAsOneOrNull()?.toDomain()
            ?: return
        queries.updateStreak(
            currentStreak = newStreak,
            longestStreak = maxOf(current.longestStreak, newStreak),
            id = LOCAL_USER_ID
        )
    }

    override suspend fun addXp(amount: Int) {
        val current = queries.getUserProfile(LOCAL_USER_ID).executeAsOneOrNull()?.toDomain()
            ?: return
        val newXp = current.totalXp + amount
        val newLevel = UserProfile.levelForXp(newXp)
        queries.updateXpAndLevel(totalXp = newXp, level = newLevel, id = LOCAL_USER_ID)
    }

    override suspend fun unlockBadge(badge: Badge) {
        val current = queries.getUserProfile(LOCAL_USER_ID).executeAsOneOrNull()?.toDomain()
            ?: return
        if (current.badges.none { it.id == badge.id && it.isUnlocked }) {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
            val updatedBadges = current.badges.toMutableList()
            val idx = updatedBadges.indexOfFirst { it.id == badge.id }
            val unlocked = badge.copy(unlockedAt = today)
            if (idx >= 0) updatedBadges[idx] = unlocked else updatedBadges.add(unlocked)
            queries.updateBadges(badges = updatedBadges, id = LOCAL_USER_ID)
        }
    }

    override suspend fun setPremiumStatus(isPremium: Boolean) {
        queries.updatePremiumStatus(isPremium = isPremium, id = LOCAL_USER_ID)
    }

    override suspend fun updateAvatar(avatarId: String) {
        queries.updateAvatarId(avatarId = avatarId, id = LOCAL_USER_ID)
    }

    override suspend fun createDefaultProfile(name: String) {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
        val profile = UserProfile(
            id = LOCAL_USER_ID,
            displayName = name,
            joinedDate = today,
            badges = Badges.ALL.map { it.copy(unlockedAt = null) }
        )
        queries.insertOrReplaceUserProfile(
            id = profile.id,
            displayName = profile.displayName,
            avatarId = profile.avatarId,
            level = profile.level,
            totalXp = profile.totalXp,
            currentStreak = profile.currentStreak,
            longestStreak = profile.longestStreak,
            isPremium = profile.isPremium,
            badges = profile.badges,
            joinedDate = profile.joinedDate
        )
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private fun ensureProfileExists() {
        if (queries.getUserProfile(LOCAL_USER_ID).executeAsOneOrNull() == null) {
            val p = defaultProfile()
            queries.insertOrReplaceUserProfile(
                id = p.id,
                displayName = p.displayName,
                avatarId = p.avatarId,
                level = p.level,
                totalXp = p.totalXp,
                currentStreak = p.currentStreak,
                longestStreak = p.longestStreak,
                isPremium = p.isPremium,
                badges = p.badges,
                joinedDate = p.joinedDate
            )
        }
    }

    private fun defaultProfile() = UserProfile(
        id = LOCAL_USER_ID,
        displayName = "Quester",
        joinedDate = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
    )

    // ─── Mapping ─────────────────────────────────────────────────────────────

    private fun com.budgetquest.db.UserProfileEntity.toDomain() = UserProfile(
        id = id,
        displayName = displayName,
        avatarId = avatarId,
        level = level,
        totalXp = totalXp,
        currentStreak = currentStreak,
        longestStreak = longestStreak,
        isPremium = isPremium,
        badges = badges,
        joinedDate = joinedDate
    )
}
