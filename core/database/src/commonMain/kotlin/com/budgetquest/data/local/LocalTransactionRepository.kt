package com.budgetquest.data.local

import com.budgetquest.domain.model.*
import com.budgetquest.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * In-memory implementation of TransactionRepository.
 * Uses MutableStateFlow to provide reactive updates.
 * Will be replaced with SQLDelight-backed implementation when DB driver is wired.
 * Backend sync layer will wrap this without changing the interface.
 */
class LocalTransactionRepository : TransactionRepository {
    private val transactions = MutableStateFlow<List<Transaction>>(emptyList())

    override fun getTransactions(monthYear: String): Flow<List<Transaction>> {
        return transactions.map { list ->
            list.filter { tx ->
                val txMonth = "${tx.date.year}-${tx.date.monthNumber.toString().padStart(2, '0')}"
                txMonth == monthYear
            }.sortedByDescending { it.date }
        }
    }

    override fun getTransactionsByCategory(
        category: TransactionCategory,
        monthYear: String
    ): Flow<List<Transaction>> {
        return transactions.map { list ->
            list.filter { tx ->
                val txMonth = "${tx.date.year}-${tx.date.monthNumber.toString().padStart(2, '0')}"
                tx.category == category && txMonth == monthYear
            }
        }
    }

    override suspend fun addTransaction(transaction: Transaction) {
        transactions.value = transactions.value + transaction
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactions.value = transactions.value.map {
            if (it.id == transaction.id) transaction else it
        }
    }

    override suspend fun deleteTransaction(id: String) {
        transactions.value = transactions.value.filter { it.id != id }
    }

    override suspend fun getTotalSpent(monthYear: String): Double {
        return transactions.value
            .filter { it.isExpense }
            .filter {
                val txMonth = "${it.date.year}-${it.date.monthNumber.toString().padStart(2, '0')}"
                txMonth == monthYear
            }
            .sumOf { it.amount }
    }

    override suspend fun getTotalSpentByCategory(
        category: TransactionCategory,
        monthYear: String
    ): Double {
        return transactions.value
            .filter { it.isExpense && it.category == category }
            .filter {
                val txMonth = "${it.date.year}-${it.date.monthNumber.toString().padStart(2, '0')}"
                txMonth == monthYear
            }
            .sumOf { it.amount }
    }
}
