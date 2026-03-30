package com.budgetquest.testing.fake

import com.budgetquest.domain.model.Quest
import com.budgetquest.domain.repository.QuestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeQuestRepository : QuestRepository {
    private val questsFlow = MutableStateFlow<List<Quest>>(emptyList())

    fun populateQuests(quests: List<Quest>) {
        questsFlow.value = quests
    }

    override fun getActiveQuests(): Flow<List<Quest>> {
        return questsFlow.map { list -> list.filter { !it.isCompleted } }
    }

    override fun getCompletedQuests(): Flow<List<Quest>> {
        return questsFlow.map { list -> list.filter { it.isCompleted } }
    }

    override suspend fun activateQuests(quests: List<Quest>) {
        questsFlow.value = questsFlow.value + quests
    }

    override suspend fun updateQuestProgress(questId: String, newValue: Double) {
        val current = questsFlow.value.toMutableList()
        val index = current.indexOfFirst { it.id == questId }
        if (index != -1) {
            current[index] = current[index].copy(currentValue = newValue)
            questsFlow.value = current
        }
    }

    override suspend fun completeQuest(questId: String) {
        val current = questsFlow.value.toMutableList()
        val index = current.indexOfFirst { it.id == questId }
        if (index != -1) {
            current[index] = current[index].copy(isCompleted = true)
            questsFlow.value = current
        }
    }

    override suspend fun clearExpiredQuests() {
        // Mock cleanup
    }
}
