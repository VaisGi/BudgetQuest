package com.budgetquest.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.budgetquest.db.BudgetQuestDatabaseQueries
import com.budgetquest.domain.model.Transaction
import com.budgetquest.domain.model.TransactionCategory
import com.budgetquest.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class SqlDelightTransactionRepository(
    private val queries: BudgetQuestDatabaseQueries
) : TransactionRepository {

    override fun getTransactions(monthYear: String): Flow<List<Transaction>> =
        queries.selectTransactionsByMonth(monthYear)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }

    override fun getTransactionsByCategory(
        category: TransactionCategory,
        monthYear: String
    ): Flow<List<Transaction>> =
        queries.selectTransactionsByCategoryAndMonth(category, monthYear)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }

    override suspend fun addTransaction(transaction: Transaction) {
        queries.insertTransaction(
            id = transaction.id,
            amount = transaction.amount,
            category = transaction.category,
            description = transaction.description,
            date = transaction.date.toString(),
            monthYear = transaction.date.toMonthYear(),
            isExpense = transaction.isExpense,
            receiptUrl = transaction.receiptUrl
        )
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        // INSERT OR REPLACE handles updates
        addTransaction(transaction)
    }

    override suspend fun deleteTransaction(id: String) {
        queries.deleteTransaction(id)
    }

    override suspend fun getTotalSpent(monthYear: String): Double =
        queries.sumTransactionsByMonth(monthYear).executeAsOne().let {
            // SUM returns null when no rows match — coerce to 0.0
            (it as? Double) ?: 0.0
        }

    override suspend fun getTotalSpentByCategory(
        category: TransactionCategory,
        monthYear: String
    ): Double =
        queries.sumTransactionsByCategoryAndMonth(category, monthYear).executeAsOne().let {
            (it as? Double) ?: 0.0
        }

    // ─── Mapping ─────────────────────────────────────────────────────────────

    private fun com.budgetquest.db.TransactionEntity.toDomain() = Transaction(
        id = id,
        amount = amount,
        category = category,
        description = description,
        date = LocalDate.parse(date),
        isExpense = isExpense,
        receiptUrl = receiptUrl
    )

    private fun LocalDate.toMonthYear(): String =
        "${year}-${monthNumber.toString().padStart(2, '0')}"
}
