package com.budgetquest.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.budgetquest.db.BudgetQuestDatabaseQueries
import com.budgetquest.domain.model.Budget
import com.budgetquest.domain.model.TransactionCategory
import com.budgetquest.domain.repository.BudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class SqlDelightBudgetRepository(
    private val queries: BudgetQuestDatabaseQueries
) : BudgetRepository {

    override fun getBudgets(monthYear: String): Flow<List<Budget>> =
        queries.selectBudgetsByMonth(monthYear)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }

    override fun getBudget(category: TransactionCategory, monthYear: String): Flow<Budget?> =
        queries.selectBudgetByCategoryAndMonth(category, monthYear)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toDomain() }

    override suspend fun setBudgetLimit(
        category: TransactionCategory,
        amount: Double,
        monthYear: String
    ) {
        val existing = queries.selectBudgetByCategoryAndMonth(category, monthYear).executeAsOneOrNull()
        if (existing != null) {
            queries.updateBudgetLimit(limitAmount = amount, category = category, monthYear = monthYear)
        } else {
            queries.insertBudget(
                id = Clock.System.now().toEpochMilliseconds().toString(),
                category = category,
                limitAmount = amount,
                spentAmount = 0.0,
                monthYear = monthYear
            )
        }
    }

    override suspend fun updateSpentAmount(
        category: TransactionCategory,
        spent: Double,
        monthYear: String
    ) {
        queries.updateBudgetSpent(spentAmount = spent, category = category, monthYear = monthYear)
    }

    override suspend fun deleteBudget(id: String) {
        queries.deleteBudget(id)
    }

    // ─── Mapping ─────────────────────────────────────────────────────────────

    private fun com.budgetquest.db.BudgetEntity.toDomain() = Budget(
        id = id,
        category = category,
        limitAmount = limitAmount,
        spentAmount = spentAmount,
        monthYear = monthYear
    )
}
