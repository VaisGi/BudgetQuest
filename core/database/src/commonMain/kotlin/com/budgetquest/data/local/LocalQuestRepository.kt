package com.budgetquest.data.local

import com.budgetquest.domain.model.Quest
import com.budgetquest.domain.repository.QuestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class LocalQuestRepository : QuestRepository {
    private val quests = MutableStateFlow<List<Quest>>(emptyList())

    override fun getActiveQuests(): Flow<List<Quest>> {
        return quests.map { list -> list.filter { !it.isCompleted } }
    }

    override fun getCompletedQuests(): Flow<List<Quest>> {
        return quests.map { list -> list.filter { it.isCompleted } }
    }

    override suspend fun activateQuests(newQuests: List<Quest>) {
        quests.value = quests.value + newQuests
    }

    override suspend fun updateQuestProgress(questId: String, newValue: Double) {
        quests.value = quests.value.map {
            if (it.id == questId) it.copy(currentValue = newValue) else it
        }
    }

    override suspend fun completeQuest(questId: String) {
        quests.value = quests.value.map {
            if (it.id == questId) it.copy(isCompleted = true) else it
        }
    }

    override suspend fun clearExpiredQuests() {
        val now = Clock.System.now().toEpochMilliseconds().toString()
        quests.value = quests.value.filter { quest ->
            val expires = quest.expiresAt
            expires == null || expires > now || quest.isCompleted
        }
    }
}
