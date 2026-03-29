package com.budgetquest.domain.repository

import com.budgetquest.domain.model.Quest
import kotlinx.coroutines.flow.Flow

/**
 * Quest repository for gamification challenges.
 */
interface QuestRepository {
    fun getActiveQuests(): Flow<List<Quest>>
    fun getCompletedQuests(): Flow<List<Quest>>
    suspend fun activateQuests(quests: List<Quest>)
    suspend fun updateQuestProgress(questId: String, newValue: Double)
    suspend fun completeQuest(questId: String)
    suspend fun clearExpiredQuests()
}
