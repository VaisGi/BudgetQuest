package com.budgetquest.testing.fake

import com.budgetquest.domain.model.Budget
import com.budgetquest.domain.model.TransactionCategory
import com.budgetquest.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeBudgetRepository : BudgetRepository {
    private val budgetsFlow = MutableStateFlow<List<Budget>>(emptyList())

    fun populateBudgets(budgets: List<Budget>) {
        budgetsFlow.value = budgets
    }

    override fun getBudgets(monthYear: String): Flow<List<Budget>> {
        return budgetsFlow.map { list ->
            list.filter { it.monthYear == monthYear || monthYear.isEmpty() }
        }
    }

    override fun getBudget(category: TransactionCategory, monthYear: String): Flow<Budget?> {
        return budgetsFlow.map { list ->
            list.find { it.category == category && (it.monthYear == monthYear || monthYear.isEmpty()) }
        }
    }

    override suspend fun setBudgetLimit(category: TransactionCategory, amount: Double, monthYear: String) {
        val current = budgetsFlow.value.toMutableList()
        val index = current.indexOfFirst { it.category == category && it.monthYear == monthYear }
        if (index != -1) {
            current[index] = current[index].copy(limitAmount = amount)
        } else {
            current.add(Budget(current.size.toString(), category, amount, 0.0, monthYear))
        }
        budgetsFlow.value = current
    }

    override suspend fun updateSpentAmount(category: TransactionCategory, spent: Double, monthYear: String) {
        val current = budgetsFlow.value.toMutableList()
        val index = current.indexOfFirst { it.category == category && it.monthYear == monthYear }
        if (index != -1) {
            current[index] = current[index].copy(spentAmount = spent)
            budgetsFlow.value = current
        }
    }

    override suspend fun deleteBudget(id: String) {
        budgetsFlow.value = budgetsFlow.value.filter { it.id != id }
    }
}
