package com.budgetquest.presentation.transaction

import com.budgetquest.domain.model.Transaction
import com.budgetquest.domain.model.TransactionCategory
import com.budgetquest.domain.usecase.AddTransactionUseCase
import com.budgetquest.domain.usecase.TrackStreakUseCase
import com.budgetquest.presentation.base.BaseViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class TransactionViewModel(
    private val addTransaction: AddTransactionUseCase,
    private val trackStreak: TrackStreakUseCase
) : BaseViewModel<TransactionConfig, TransactionEvent>(TransactionConfig()) {

    override fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.OnAmountChanged -> {
                val isValid = event.amount.toDoubleOrNull()?.let { it > 0 } ?: false
                updateState {
                    copy(
                        amount = event.amount,
                        isAmountValid = isValid,
                        errorMessage = null
                    )
                }
            }
            is TransactionEvent.OnDescriptionChanged -> {
                updateState { copy(description = event.description) }
            }
            is TransactionEvent.OnCategorySelected -> {
                updateState {
                    copy(
                        selectedCategory = event.category,
                        categories = categories.map {
                            it.copy(isSelected = it.category == event.category)
                        }
                    )
                }
            }
            is TransactionEvent.OnTypeToggled -> {
                updateState { copy(isExpense = event.isExpense) }
            }
            is TransactionEvent.OnSaveTapped -> saveTransaction()
            is TransactionEvent.OnDismissXpAnimation -> {
                updateState { copy(showXpAnimation = false) }
            }
        }
    }

    private fun saveTransaction() {
        val currentState = state.value
        val parsedAmount = currentState.amount.toDoubleOrNull()
        if (parsedAmount == null || parsedAmount <= 0) {
            updateState { copy(errorMessage = "Please enter a valid amount") }
            return
        }

        updateState { copy(isSaving = true) }

        launch {
            try {
                val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
                val transaction = Transaction(
                    id = generateId(),
                    amount = parsedAmount,
                    category = currentState.selectedCategory,
                    description = currentState.description.ifBlank { currentState.selectedCategory.displayName },
                    date = today,
                    isExpense = currentState.isExpense
                )

                addTransaction(transaction)
                trackStreak.recordDailyActivity()

                updateState {
                    copy(
                        isSaving = false,
                        saveSuccess = true,
                        xpEarned = AddTransactionUseCase.XP_PER_TRANSACTION,
                        showXpAnimation = true
                    )
                }
            } catch (e: Exception) {
                updateState {
                    copy(
                        isSaving = false,
                        errorMessage = "Failed to save: ${e.message}"
                    )
                }
            }
        }
    }

    private fun generateId(): String {
        return Clock.System.now().toEpochMilliseconds().toString()
    }
}
