package com.budgetquest.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.budgetquest.db.BudgetQuestDatabaseQueries
import com.budgetquest.domain.model.SavingsGoal
import com.budgetquest.domain.repository.SavingsGoalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SqlDelightSavingsGoalRepository(
    private val queries: BudgetQuestDatabaseQueries
) : SavingsGoalRepository {

    override fun getGoals(): Flow<List<SavingsGoal>> =
        queries.selectAllSavingsGoals()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.map { it.toDomain() } }

    override fun getGoal(id: String): Flow<SavingsGoal?> =
        queries.selectAllSavingsGoals()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities -> entities.firstOrNull { it.id == id }?.toDomain() }

    override suspend fun createGoal(goal: SavingsGoal) {
        queries.insertSavingsGoal(
            id = goal.id,
            name = goal.name,
            emoji = goal.emoji,
            targetAmount = goal.targetAmount,
            savedAmount = goal.savedAmount,
            deadlineMonthYear = goal.deadlineMonthYear
        )
    }

    override suspend fun addSavings(goalId: String, amount: Double) {
        val current = queries.selectAllSavingsGoals().executeAsList()
            .firstOrNull { it.id == goalId } ?: return
        queries.updateSavingsAmount(
            savedAmount = current.savedAmount + amount,
            id = goalId
        )
    }

    override suspend fun deleteGoal(id: String) {
        queries.deleteSavingsGoal(id)
    }

    override suspend fun getActiveGoalCount(): Int =
        queries.selectAllSavingsGoals().executeAsList()
            .count { it.savedAmount < it.targetAmount }

    // ─── Mapping ─────────────────────────────────────────────────────────────

    private fun com.budgetquest.db.SavingsGoalEntity.toDomain() = SavingsGoal(
        id = id,
        name = name,
        emoji = emoji,
        targetAmount = targetAmount,
        savedAmount = savedAmount,
        deadlineMonthYear = deadlineMonthYear
    )
}
