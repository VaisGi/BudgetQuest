package com.budgetquest.domain.usecase

import com.budgetquest.domain.model.Transaction
import com.budgetquest.domain.model.Badges
import com.budgetquest.domain.repository.TransactionRepository
import com.budgetquest.domain.repository.BudgetRepository
import com.budgetquest.domain.repository.UserRepository

class AddTransactionUseCase(
    private val transactionRepo: TransactionRepository,
    private val budgetRepo: BudgetRepository,
    private val userRepo: UserRepository
) {
    companion object {
        const val XP_PER_TRANSACTION = 10
    }

    suspend operator fun invoke(transaction: Transaction) {
        // 1. Persist the transaction
        transactionRepo.addTransaction(transaction)

        // 2. Update budget spent amount for the category
        if (transaction.isExpense) {
            val monthYear = "${transaction.date.year}-${transaction.date.monthNumber.toString().padStart(2, '0')}"
            val totalSpent = transactionRepo.getTotalSpentByCategory(transaction.category, monthYear)
            budgetRepo.updateSpentAmount(transaction.category, totalSpent, monthYear)
        }

        // 3. Award XP for logging
        userRepo.addXp(XP_PER_TRANSACTION)

        // 4. Check for first transaction badge
        userRepo.unlockBadge(Badges.FIRST_TRANSACTION)
    }
}
