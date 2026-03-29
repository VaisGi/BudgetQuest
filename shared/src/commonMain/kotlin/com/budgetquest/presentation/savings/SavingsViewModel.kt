package com.budgetquest.presentation.savings

import com.budgetquest.domain.model.Badges
import com.budgetquest.domain.model.SavingsGoal
import com.budgetquest.domain.repository.SavingsGoalRepository
import com.budgetquest.domain.repository.UserRepository
import com.budgetquest.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

class SavingsViewModel(
    private val savingsRepo: SavingsGoalRepository,
    private val userRepo: UserRepository
) : BaseViewModel<SavingsConfig, SavingsEvent>(SavingsConfig()) {

    override fun onEvent(event: SavingsEvent) {
        when (event) {
            is SavingsEvent.OnScreenLoaded -> loadGoals()
            is SavingsEvent.OnAddGoalTapped -> handleAddGoalTapped()
            is SavingsEvent.OnGoalNameChanged -> updateState { copy(newGoalName = event.name) }
            is SavingsEvent.OnGoalEmojiChanged -> updateState { copy(newGoalEmoji = event.emoji) }
            is SavingsEvent.OnGoalTargetChanged -> updateState { copy(newGoalTarget = event.target) }
            is SavingsEvent.OnSaveGoal -> saveGoal()
            is SavingsEvent.OnDismissAddGoal -> updateState { copy(showAddGoal = false) }
            is SavingsEvent.OnAddSavings -> addSavings(event.goalId, event.amount)
            is SavingsEvent.OnDeleteGoal -> deleteGoal(event.goalId)
            is SavingsEvent.OnDismissMilestone -> updateState {
                copy(showMilestoneAnimation = false, reachedMilestone = null)
            }
        }
    }

    private fun loadGoals() {
        launch {
            val profile = userRepo.getUserProfile().first()

            savingsRepo.getGoals().collect { goals ->
                val goalCount = goals.size
                val canAdd = profile.isPremium || goalCount < 1

                updateState {
                    copy(
                        goals = goals.map { g ->
                            SavingsGoalItemConfig(
                                id = g.id,
                                name = g.name,
                                emoji = g.emoji,
                                savedAmount = g.savedAmount,
                                targetAmount = g.targetAmount,
                                progress = g.progress,
                                milestone = g.milestone,
                                isCompleted = g.isCompleted
                            )
                        },
                        isLoading = false,
                        isPremiumUser = profile.isPremium,
                        canAddMoreGoals = canAdd
                    )
                }
            }
        }
    }

    private fun handleAddGoalTapped() {
        val currentState = state.value
        if (!currentState.canAddMoreGoals) {
            updateState { copy(showPremiumGate = true) }
        } else {
            updateState { copy(showAddGoal = true) }
        }
    }

    private fun saveGoal() {
        val currentState = state.value
        val target = currentState.newGoalTarget.toDoubleOrNull() ?: return
        if (currentState.newGoalName.isBlank()) return

        launch {
            val goal = SavingsGoal(
                id = Clock.System.now().toEpochMilliseconds().toString(),
                name = currentState.newGoalName,
                emoji = currentState.newGoalEmoji,
                targetAmount = target,
                savedAmount = 0.0
            )
            savingsRepo.createGoal(goal)
            userRepo.unlockBadge(Badges.SAVINGS_FIRST)
            updateState {
                copy(showAddGoal = false, newGoalName = "", newGoalTarget = "", newGoalEmoji = "🎯")
            }
        }
    }

    private fun addSavings(goalId: String, amount: Double) {
        launch {
            val goalBefore = savingsRepo.getGoal(goalId).first() ?: return@launch
            val milestoneBefore = goalBefore.milestone

            savingsRepo.addSavings(goalId, amount)
            userRepo.addXp(25) // XP for saving

            val goalAfter = savingsRepo.getGoal(goalId).first() ?: return@launch
            if (goalAfter.milestone != milestoneBefore) {
                updateState {
                    copy(showMilestoneAnimation = true, reachedMilestone = goalAfter.milestone)
                }
                if (goalAfter.isCompleted) {
                    userRepo.unlockBadge(Badges.SAVINGS_COMPLETE)
                    userRepo.addXp(200) // Bonus XP for completing goal
                }
            }
        }
    }

    private fun deleteGoal(goalId: String) {
        launch { savingsRepo.deleteGoal(goalId) }
    }
}
