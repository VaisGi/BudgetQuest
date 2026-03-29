package com.budgetquest.presentation.dashboard

import com.budgetquest.domain.usecase.CalculateBudgetUseCase
import com.budgetquest.domain.usecase.GetQuestsUseCase
import com.budgetquest.domain.usecase.TrackStreakUseCase
import com.budgetquest.domain.repository.UserRepository
import com.budgetquest.domain.repository.SavingsGoalRepository
import com.budgetquest.domain.repository.TransactionRepository
import com.budgetquest.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class DashboardViewModel(
    private val calculateBudget: CalculateBudgetUseCase,
    private val getQuests: GetQuestsUseCase,
    private val trackStreak: TrackStreakUseCase,
    private val userRepo: UserRepository,
    private val savingsRepo: SavingsGoalRepository,
    private val transactionRepo: TransactionRepository
) : BaseViewModel<DashboardConfig, DashboardEvent>(DashboardConfig()) {

    override fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.OnScreenLoaded -> loadDashboard()
            is DashboardEvent.OnQuestTapped -> { /* Navigation handled by platform */ }
            is DashboardEvent.OnAddTransactionTapped -> { /* Navigation handled by platform */ }
            is DashboardEvent.OnUpgradeTapped -> { /* Navigation handled by platform */ }
            is DashboardEvent.OnViewAllBudgets -> { /* Navigation handled by platform */ }
            is DashboardEvent.OnViewAllQuests -> { /* Navigation handled by platform */ }
            is DashboardEvent.OnProfileTapped -> { /* Navigation handled by platform */ }
        }
    }

    private fun loadDashboard() {
        launch {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val monthYear = "${today.year}-${today.monthNumber.toString().padStart(2, '0')}"

            // Generate weekly quests if needed
            getQuests.generateWeeklyQuests()

            // Combine multiple reactive flows into a single dashboard state
            combine(
                userRepo.getUserProfile(),
                calculateBudget(monthYear),
                getQuests.getActiveQuests(),
                savingsRepo.getGoals(),
                transactionRepo.getTransactions(monthYear)
            ) { profile, budgets, quests, goals, transactions ->

                val todayTransactions = transactions.filter {
                    it.date == today && it.isExpense
                }

                val greetingText = when (today.dayOfWeek.ordinal) {
                    0, 6 -> "Happy weekend, ${profile.displayName}! 🎉"
                    else -> "Let's crush it, ${profile.displayName}! 💪"
                }

                DashboardConfig(
                    greeting = greetingText,
                    streakDays = profile.currentStreak,
                    streakBadgeVisible = profile.currentStreak >= 3,
                    xpProgress = profile.xpProgressInLevel,
                    currentLevel = profile.level,
                    totalXp = profile.totalXp,
                    budgetCards = budgets.take(4).map { budget ->
                        BudgetCardConfig(
                            id = budget.id,
                            categoryName = budget.category.displayName,
                            categoryEmoji = budget.category.emoji,
                            spent = budget.spentAmount,
                            limit = budget.limitAmount,
                            percentUsed = budget.percentUsed,
                            status = budget.statusColor
                        )
                    },
                    activeQuests = quests.take(3).map { quest ->
                        QuestCardConfig(
                            id = quest.id,
                            title = quest.title,
                            emoji = quest.emoji,
                            progress = quest.progress,
                            xpReward = quest.xpReward,
                            isCompleted = quest.isCompleted
                        )
                    },
                    savingsGoalName = goals.firstOrNull()?.name ?: "",
                    savingsGoalProgress = goals.firstOrNull()?.progress ?: 0f,
                    savingsGoalEmoji = goals.firstOrNull()?.emoji ?: "🎯",
                    hasSavingsGoal = goals.isNotEmpty(),
                    isPremiumUser = profile.isPremium,
                    showUpgradePrompt = !profile.isPremium,
                    isLoading = false,
                    todaySpent = todayTransactions.sumOf { it.amount },
                    monthlySpent = transactions.filter { it.isExpense }.sumOf { it.amount },
                    monthlyBudgetTotal = budgets.sumOf { it.limitAmount }
                )
            }.collect { config ->
                updateState { config }
            }
        }
    }
}
