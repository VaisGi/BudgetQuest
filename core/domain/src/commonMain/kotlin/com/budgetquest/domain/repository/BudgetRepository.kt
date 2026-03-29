package com.budgetquest.domain.repository

import com.budgetquest.domain.model.Budget
import com.budgetquest.domain.model.TransactionCategory
import kotlinx.coroutines.flow.Flow

/**
 * Budget repository interface.
 * Local-first, designed for seamless backend sync later.
 */
interface BudgetRepository {
    fun getBudgets(monthYear: String): Flow<List<Budget>>
    fun getBudget(category: TransactionCategory, monthYear: String): Flow<Budget?>
    suspend fun setBudgetLimit(category: TransactionCategory, amount: Double, monthYear: String)
    suspend fun updateSpentAmount(category: TransactionCategory, spent: Double, monthYear: String)
    suspend fun deleteBudget(id: String)
}
