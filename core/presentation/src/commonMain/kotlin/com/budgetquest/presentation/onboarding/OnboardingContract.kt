package com.budgetquest.presentation.onboarding

data class OnboardingConfig(
    val currentStep: Int = 0,
    val totalSteps: Int = 4,
    val financialGoal: FinancialGoal? = null,
    val monthlyIncome: IncomeRange? = null,
    val displayName: String = "",
    val isComplete: Boolean = false,
    val showPaywall: Boolean = false
)

enum class FinancialGoal(val displayName: String, val emoji: String, val description: String) {
    SAVE("Save Money", "💰", "Build an emergency fund or save for something special"),
    BUDGET("Track Budget", "📊", "Know where your money goes and stick to limits"),
    PAY_DEBT("Pay Off Debt", "🎯", "Snowball your way out of debt with a plan"),
    ALL("All of the Above", "🚀", "Master your complete financial picture")
}

enum class IncomeRange(val displayName: String) {
    UNDER_2K("Under $2,000/mo"),
    FROM_2K_TO_4K("$2,000 – $4,000/mo"),
    FROM_4K_TO_7K("$4,000 – $7,000/mo"),
    FROM_7K_TO_10K("$7,000 – $10,000/mo"),
    OVER_10K("$10,000+/mo"),
    PREFER_NOT("Prefer not to say")
}

sealed class OnboardingEvent {
    data class OnGoalSelected(val goal: FinancialGoal) : OnboardingEvent()
    data class OnIncomeSelected(val income: IncomeRange) : OnboardingEvent()
    data class OnNameEntered(val name: String) : OnboardingEvent()
    data object OnNextStep : OnboardingEvent()
    data object OnPreviousStep : OnboardingEvent()
    data object OnComplete : OnboardingEvent()
    data object OnSkipPaywall : OnboardingEvent()
    data object OnSubscribe : OnboardingEvent()
}
