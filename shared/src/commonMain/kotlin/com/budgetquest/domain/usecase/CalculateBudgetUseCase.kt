package com.budgetquest.domain.usecase

import com.budgetquest.domain.model.Budget
import com.budgetquest.domain.model.TransactionCategory
import com.budgetquest.domain.repository.BudgetRepository
import com.budgetquest.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class CalculateBudgetUseCase(
    private val budgetRepo: BudgetRepository,
    private val transactionRepo: TransactionRepository
) {
    /**
     * Returns a reactive flow of budgets with real-time spent amounts
     * computed from actual transactions.
     */
    operator fun invoke(monthYear: String): Flow<List<Budget>> {
        return combine(
            budgetRepo.getBudgets(monthYear),
            transactionRepo.getTransactions(monthYear)
        ) { budgets, transactions ->
            budgets.map { budget ->
                val actualSpent = transactions
                    .filter { it.isExpense && it.category == budget.category }
                    .sumOf { it.amount }
                budget.copy(spentAmount = actualSpent)
            }
        }
    }
}
