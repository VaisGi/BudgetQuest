package com.budgetquest.presentation.onboarding

import com.budgetquest.domain.repository.UserRepository
import com.budgetquest.presentation.base.BaseViewModel

class OnboardingViewModel(
    private val userRepo: UserRepository
) : BaseViewModel<OnboardingConfig, OnboardingEvent>(OnboardingConfig()) {

    override fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.OnGoalSelected -> updateState { copy(financialGoal = event.goal) }
            is OnboardingEvent.OnIncomeSelected -> updateState { copy(monthlyIncome = event.income) }
            is OnboardingEvent.OnNameEntered -> updateState { copy(displayName = event.name) }
            is OnboardingEvent.OnNextStep -> {
                val next = state.value.currentStep + 1
                if (next < state.value.totalSteps) {
                    updateState { copy(currentStep = next) }
                } else {
                    // Last step → show paywall
                    updateState { copy(showPaywall = true) }
                }
            }
            is OnboardingEvent.OnPreviousStep -> {
                val prev = (state.value.currentStep - 1).coerceAtLeast(0)
                updateState { copy(currentStep = prev) }
            }
            is OnboardingEvent.OnComplete -> completeOnboarding()
            is OnboardingEvent.OnSkipPaywall -> completeOnboarding()
            is OnboardingEvent.OnSubscribe -> {
                // RevenueCat purchase flow will be triggered by platform
                // After successful purchase, call completeOnboarding
                completeOnboarding()
            }
        }
    }

    private fun completeOnboarding() {
        launch {
            val name = state.value.displayName.ifBlank { "Quester" }
            userRepo.createDefaultProfile(name)
            updateState { copy(isComplete = true) }
        }
    }
}
