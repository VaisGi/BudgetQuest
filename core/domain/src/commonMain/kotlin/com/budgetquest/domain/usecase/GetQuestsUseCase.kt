package com.budgetquest.domain.usecase

import com.budgetquest.domain.model.Badges
import com.budgetquest.domain.model.Quest
import com.budgetquest.domain.model.QuestTemplates
import com.budgetquest.domain.repository.QuestRepository
import com.budgetquest.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class GetQuestsUseCase(
    private val questRepo: QuestRepository,
    private val userRepo: UserRepository
) {
    companion object {
        const val XP_QUEST_COMPLETE = 100
    }

    fun getActiveQuests(): Flow<List<Quest>> = questRepo.getActiveQuests()

    fun getCompletedQuests(): Flow<List<Quest>> = questRepo.getCompletedQuests()

    /**
     * Generate and activate weekly quests from templates.
     */
    suspend fun generateWeeklyQuests() {
        val active = questRepo.getActiveQuests().first()
        if (active.isEmpty()) {
            val templates = QuestTemplates.weeklyQuests().shuffled().take(3)
            questRepo.activateQuests(templates)
        }
    }

    /**
     * Update quest progress and check for completion.
     */
    suspend fun updateProgress(questId: String, newValue: Double) {
        questRepo.updateQuestProgress(questId, newValue)

        val quests = questRepo.getActiveQuests().first()
        val quest = quests.find { it.id == questId }
        if (quest != null && newValue >= quest.targetValue && !quest.isCompleted) {
            questRepo.completeQuest(questId)
            userRepo.addXp(quest.xpReward)
            userRepo.unlockBadge(Badges.QUEST_COMPLETE)
        }
    }
}
