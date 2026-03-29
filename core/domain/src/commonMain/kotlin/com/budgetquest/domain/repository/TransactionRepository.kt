package com.budgetquest.domain.repository

import com.budgetquest.domain.model.Transaction
import com.budgetquest.domain.model.TransactionCategory
import kotlinx.coroutines.flow.Flow

/**
 * Transaction repository interface.
 * Currently backed by local SQLDelight storage.
 * Designed for future backend integration — add remote sync without changing consumers.
 */
interface TransactionRepository {
    fun getTransactions(monthYear: String): Flow<List<Transaction>>
    fun getTransactionsByCategory(category: TransactionCategory, monthYear: String): Flow<List<Transaction>>
    suspend fun addTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: String)
    suspend fun getTotalSpent(monthYear: String): Double
    suspend fun getTotalSpentByCategory(category: TransactionCategory, monthYear: String): Double
}
