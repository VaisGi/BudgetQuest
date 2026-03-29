package com.budgetquest.domain.repository

import com.budgetquest.domain.model.SavingsGoal
import kotlinx.coroutines.flow.Flow

/**
 * SavingsGoal repository interface.
 * Free tier: max 1 active goal. Premium: unlimited.
 */
interface SavingsGoalRepository {
    fun getGoals(): Flow<List<SavingsGoal>>
    fun getGoal(id: String): Flow<SavingsGoal?>
    suspend fun createGoal(goal: SavingsGoal)
    suspend fun addSavings(goalId: String, amount: Double)
    suspend fun deleteGoal(id: String)
    suspend fun getActiveGoalCount(): Int
}
