package com.budgetquest.data.local

import com.budgetquest.domain.model.SavingsGoal
import com.budgetquest.domain.repository.SavingsGoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class LocalSavingsGoalRepository : SavingsGoalRepository {
    private val goals = MutableStateFlow<List<SavingsGoal>>(emptyList())

    override fun getGoals(): Flow<List<SavingsGoal>> = goals

    override fun getGoal(id: String): Flow<SavingsGoal?> {
        return goals.map { list -> list.find { it.id == id } }
    }

    override suspend fun createGoal(goal: SavingsGoal) {
        goals.value = goals.value + goal
    }

    override suspend fun addSavings(goalId: String, amount: Double) {
        goals.value = goals.value.map {
            if (it.id == goalId) it.copy(savedAmount = it.savedAmount + amount) else it
        }
    }

    override suspend fun deleteGoal(id: String) {
        goals.value = goals.value.filter { it.id != id }
    }

    override suspend fun getActiveGoalCount(): Int {
        return goals.value.size
    }
}
