package com.budgetquest.presentation.budget

import com.budgetquest.domain.repository.BudgetRepository
import com.budgetquest.domain.usecase.CalculateBudgetUseCase
import com.budgetquest.presentation.base.BaseViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class BudgetViewModel(
    private val calculateBudget: CalculateBudgetUseCase,
    private val budgetRepo: BudgetRepository
) : BaseViewModel<BudgetConfig, BudgetEvent>(BudgetConfig()) {

    override fun onEvent(event: BudgetEvent) {
        when (event) {
            is BudgetEvent.OnScreenLoaded -> loadBudgets()
            is BudgetEvent.OnAddBudgetTapped -> updateState { copy(showAddBudget = true) }
            is BudgetEvent.OnDismissAddBudget -> updateState { copy(showAddBudget = false) }
            is BudgetEvent.OnNewBudgetCategorySelected -> updateState { copy(newBudgetCategory = event.category) }
            is BudgetEvent.OnNewBudgetAmountChanged -> updateState { copy(newBudgetAmount = event.amount) }
            is BudgetEvent.OnSaveBudget -> saveBudget()
            is BudgetEvent.OnDeleteBudget -> deleteBudget(event.id)
        }
    }

    private fun loadBudgets() {
        launch {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val monthYear = "${today.year}-${today.monthNumber.toString().padStart(2, '0')}"

            calculateBudget(monthYear).collect { budgets ->
                val totalSpent = budgets.sumOf { it.spentAmount }
                val totalLimit = budgets.sumOf { it.limitAmount }
                val overallPercent = if (totalLimit > 0) (totalSpent / totalLimit).toFloat().coerceIn(0f, 1f) else 0f

                updateState {
                    copy(
                        budgets = budgets.map { b ->
                            BudgetItemConfig(
                                id = b.id,
                                categoryName = b.category.displayName,
                                categoryEmoji = b.category.emoji,
                                spent = b.spentAmount,
                                limit = b.limitAmount,
                                percentUsed = b.percentUsed,
                                status = b.statusColor,
                                remaining = b.remainingAmount
                            )
                        },
                        totalSpent = totalSpent,
                        totalLimit = totalLimit,
                        overallPercent = overallPercent,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun saveBudget() {
        val currentState = state.value
        val amount = currentState.newBudgetAmount.toDoubleOrNull() ?: return

        launch {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val monthYear = "${today.year}-${today.monthNumber.toString().padStart(2, '0')}"
            budgetRepo.setBudgetLimit(currentState.newBudgetCategory, amount, monthYear)
            updateState { copy(showAddBudget = false, newBudgetAmount = "") }
        }
    }

    private fun deleteBudget(id: String) {
        launch {
            budgetRepo.deleteBudget(id)
        }
    }
}
