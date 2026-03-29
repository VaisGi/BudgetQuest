package com.budgetquest.presentation.transaction

import com.budgetquest.domain.model.TransactionCategory

/**
 * UI contract for the Add/Edit Transaction screen.
 */
data class TransactionConfig(
    val amount: String = "",
    val description: String = "",
    val selectedCategory: TransactionCategory = TransactionCategory.OTHER,
    val isExpense: Boolean = true,
    val categories: List<CategoryOptionConfig> = TransactionCategory.entries.map {
        CategoryOptionConfig(it, it.displayName, it.emoji, false)
    },
    val isAmountValid: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null,
    val xpEarned: Int = 0,
    val showXpAnimation: Boolean = false
)

data class CategoryOptionConfig(
    val category: TransactionCategory,
    val name: String,
    val emoji: String,
    val isSelected: Boolean
)

sealed class TransactionEvent {
    data class OnAmountChanged(val amount: String) : TransactionEvent()
    data class OnDescriptionChanged(val description: String) : TransactionEvent()
    data class OnCategorySelected(val category: TransactionCategory) : TransactionEvent()
    data class OnTypeToggled(val isExpense: Boolean) : TransactionEvent()
    data object OnSaveTapped : TransactionEvent()
    data object OnDismissXpAnimation : TransactionEvent()
}
