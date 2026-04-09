package com.budgetquest.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.budgetquest.db.BudgetQuestDatabaseQueries
import com.budgetquest.domain.model.Quest
import com.budgetquest.domain.repository.QuestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class SqlDelightQuestRepository(
    private val queries: BudgetQuestDatabaseQueries
) : QuestRepository {

    override fun getActiveQuests(): Flow<List<Quest>> =
        queries.selectActiveQuests()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }

    override fun getCompletedQuests(): Flow<List<Quest>> =
        queries.selectCompletedQuests()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }

    override suspend fun activateQuests(newQuests: List<Quest>) {
        newQuests.forEach { quest ->
            queries.insertQuest(
                id = quest.id,
                title = quest.title,
                description = quest.description,
                emoji = quest.emoji,
                xpReward = quest.xpReward,
                type = quest.type,
                targetValue = quest.targetValue,
                currentValue = quest.currentValue,
                isCompleted = quest.isCompleted,
                expiresAt = quest.expiresAt
            )
        }
    }

    override suspend fun updateQuestProgress(questId: String, newValue: Double) {
        queries.updateQuestProgress(currentValue = newValue, id = questId)
    }

    override suspend fun completeQuest(questId: String) {
        queries.completeQuest(questId)
    }

    override suspend fun clearExpiredQuests() {
        val now = Clock.System.now().toString()
        queries.deleteExpiredQuests(now)
    }

    // ─── Mapping ─────────────────────────────────────────────────────────────

    private fun com.budgetquest.db.QuestEntity.toDomain() = Quest(
        id = id,
        title = title,
        description = description,
        emoji = emoji,
        xpReward = xpReward,
        type = type,
        targetValue = targetValue,
        currentValue = currentValue,
        isCompleted = isCompleted,
        expiresAt = expiresAt
    )
}
