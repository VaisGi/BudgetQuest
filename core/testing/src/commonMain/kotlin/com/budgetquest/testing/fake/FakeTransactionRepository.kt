package com.budgetquest.testing.fake

import com.budgetquest.domain.model.Transaction
import com.budgetquest.domain.model.TransactionCategory
import com.budgetquest.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeTransactionRepository : TransactionRepository {
    private val transactionsFlow = MutableStateFlow<List<Transaction>>(emptyList())
    
    // Test helper to initially seed data
    fun populateTransactions(transactions: List<Transaction>) {
        transactionsFlow.value = transactions
    }

    override fun getTransactions(monthYear: String): Flow<List<Transaction>> {
        return transactionsFlow.map { list ->
            // simple string match or just return all for fake bounds
            list.filter { it.date.toString().contains(monthYear) || monthYear.isEmpty() }
        }
    }

    override fun getTransactionsByCategory(category: TransactionCategory, monthYear: String): Flow<List<Transaction>> {
        return transactionsFlow.map { list ->
            list.filter { it.category == category && (it.date.toString().contains(monthYear) || monthYear.isEmpty()) }
        }
    }

    override suspend fun addTransaction(transaction: Transaction) {
        transactionsFlow.value = transactionsFlow.value + transaction
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        val updated = transactionsFlow.value.toMutableList()
        val index = updated.indexOfFirst { it.id == transaction.id }
        if (index != -1) {
            updated[index] = transaction
            transactionsFlow.value = updated
        }
    }

    override suspend fun deleteTransaction(id: String) {
        transactionsFlow.value = transactionsFlow.value.filter { it.id != id }
    }

    override suspend fun getTotalSpent(monthYear: String): Double {
        return transactionsFlow.value
            .filter { (it.date.toString().contains(monthYear) || monthYear.isEmpty()) && it.isExpense }
            .sumOf { it.amount }
    }

    override suspend fun getTotalSpentByCategory(category: TransactionCategory, monthYear: String): Double {
        return transactionsFlow.value
            .filter { it.category == category && (it.date.toString().contains(monthYear) || monthYear.isEmpty()) && it.isExpense }
            .sumOf { it.amount }
    }
}
