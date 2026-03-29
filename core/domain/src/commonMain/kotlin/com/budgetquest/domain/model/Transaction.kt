package com.budgetquest.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: String,
    val amount: Double,
    val category: TransactionCategory,
    val description: String,
    val date: LocalDate,
    val isExpense: Boolean = true,
    val receiptUrl: String? = null
)

@Serializable
enum class TransactionCategory(val displayName: String, val emoji: String) {
    FOOD("Food & Dining", "🍕"),
    TRANSPORT("Transport", "🚗"),
    SHOPPING("Shopping", "🛍️"),
    ENTERTAINMENT("Entertainment", "🎮"),
    BILLS("Bills & Utilities", "💡"),
    HEALTH("Health", "💊"),
    EDUCATION("Education", "📚"),
    SAVINGS("Savings", "💰"),
    INCOME("Income", "💵"),
    OTHER("Other", "📦")
}
