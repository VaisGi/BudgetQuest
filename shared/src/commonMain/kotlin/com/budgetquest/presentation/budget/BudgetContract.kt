package com.budgetquest.presentation.budget

import com.budgetquest.domain.model.BudgetStatus
import com.budgetquest.domain.model.TransactionCategory

data class BudgetConfig(
    val budgets: List<BudgetItemConfig> = emptyList(),
    val totalSpent: Double = 0.0,
    val totalLimit: Double = 0.0,
    val overallPercent: Float = 0f,
    val isLoading: Boolean = true,
    val showAddBudget: Boolean = false,
    val newBudgetCategory: TransactionCategory = TransactionCategory.FOOD,
    val newBudgetAmount: String = ""
)

data class BudgetItemConfig(
    val id: String,
    val categoryName: String,
    val categoryEmoji: String,
    val spent: Double,
    val limit: Double,
    val percentUsed: Float,
    val status: BudgetStatus,
    val remaining: Double
)

sealed class BudgetEvent {
    data object OnScreenLoaded : BudgetEvent()
    data object OnAddBudgetTapped : BudgetEvent()
    data class OnNewBudgetCategorySelected(val category: TransactionCategory) : BudgetEvent()
    data class OnNewBudgetAmountChanged(val amount: String) : BudgetEvent()
    data object OnSaveBudget : BudgetEvent()
    data object OnDismissAddBudget : BudgetEvent()
    data class OnDeleteBudget(val id: String) : BudgetEvent()
}
