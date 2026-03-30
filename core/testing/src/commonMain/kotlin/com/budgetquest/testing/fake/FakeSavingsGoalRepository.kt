package com.budgetquest.testing.fake

import com.budgetquest.domain.model.SavingsGoal
import com.budgetquest.domain.repository.SavingsGoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeSavingsGoalRepository : SavingsGoalRepository {
    private val goalsFlow = MutableStateFlow<List<SavingsGoal>>(emptyList())

    fun populateGoals(goals: List<SavingsGoal>) {
        goalsFlow.value = goals
    }

    override fun getGoals(): Flow<List<SavingsGoal>> = goalsFlow

    override fun getGoal(id: String): Flow<SavingsGoal?> {
        return goalsFlow.map { list -> list.find { it.id == id } }
    }

    override suspend fun createGoal(goal: SavingsGoal) {
        goalsFlow.value = goalsFlow.value + goal
    }

    override suspend fun addSavings(goalId: String, amount: Double) {
        val current = goalsFlow.value.toMutableList()
        val index = current.indexOfFirst { it.id == goalId }
        if (index != -1) {
            val goal = current[index]
            current[index] = goal.copy(savedAmount = goal.savedAmount + amount)
            goalsFlow.value = current
        }
    }

    override suspend fun deleteGoal(id: String) {
        goalsFlow.value = goalsFlow.value.filter { it.id != id }
    }

    override suspend fun getActiveGoalCount(): Int {
        return goalsFlow.value.count { it.savedAmount < it.targetAmount }
    }
}
