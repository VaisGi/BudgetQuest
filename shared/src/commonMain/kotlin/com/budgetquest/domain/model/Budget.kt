package com.budgetquest.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Budget(
    val id: String,
    val category: TransactionCategory,
    val limitAmount: Double,
    val spentAmount: Double,
    val monthYear: String // "2026-03"
) {
    val remainingAmount: Double get() = limitAmount - spentAmount
    val percentUsed: Float get() = if (limitAmount > 0) (spentAmount / limitAmount).toFloat().coerceIn(0f, 1f) else 0f
    val isOverBudget: Boolean get() = spentAmount > limitAmount
    val statusColor: BudgetStatus get() = when {
        percentUsed >= 1.0f -> BudgetStatus.OVER
        percentUsed >= 0.8f -> BudgetStatus.WARNING
        percentUsed >= 0.5f -> BudgetStatus.MODERATE
        else -> BudgetStatus.HEALTHY
    }
}

@Serializable
enum class BudgetStatus {
    HEALTHY, MODERATE, WARNING, OVER
}
