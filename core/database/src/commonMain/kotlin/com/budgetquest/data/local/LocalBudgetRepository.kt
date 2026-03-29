package com.budgetquest.data.local

import com.budgetquest.domain.model.Budget
import com.budgetquest.domain.model.TransactionCategory
import com.budgetquest.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class LocalBudgetRepository : BudgetRepository {
    private val budgets = MutableStateFlow<List<Budget>>(emptyList())

    override fun getBudgets(monthYear: String): Flow<List<Budget>> {
        return budgets.map { list -> list.filter { it.monthYear == monthYear } }
    }

    override fun getBudget(category: TransactionCategory, monthYear: String): Flow<Budget?> {
        return budgets.map { list ->
            list.find { it.category == category && it.monthYear == monthYear }
        }
    }

    override suspend fun setBudgetLimit(category: TransactionCategory, amount: Double, monthYear: String) {
        val existing = budgets.value.find { it.category == category && it.monthYear == monthYear }
        if (existing != null) {
            budgets.value = budgets.value.map {
                if (it.id == existing.id) it.copy(limitAmount = amount) else it
            }
        } else {
            val newBudget = Budget(
                id = Clock.System.now().toEpochMilliseconds().toString(),
                category = category,
                limitAmount = amount,
                spentAmount = 0.0,
                monthYear = monthYear
            )
            budgets.value = budgets.value + newBudget
        }
    }

    override suspend fun updateSpentAmount(category: TransactionCategory, spent: Double, monthYear: String) {
        budgets.value = budgets.value.map {
            if (it.category == category && it.monthYear == monthYear) {
                it.copy(spentAmount = spent)
            } else it
        }
    }

    override suspend fun deleteBudget(id: String) {
        budgets.value = budgets.value.filter { it.id != id }
    }
}
