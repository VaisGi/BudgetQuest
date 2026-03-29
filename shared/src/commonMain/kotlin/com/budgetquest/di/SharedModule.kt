package com.budgetquest.di

import com.budgetquest.data.local.*
import com.budgetquest.domain.repository.*
import com.budgetquest.domain.usecase.*
import com.budgetquest.presentation.budget.BudgetViewModel
import com.budgetquest.presentation.dashboard.DashboardViewModel
import com.budgetquest.presentation.onboarding.OnboardingViewModel
import com.budgetquest.presentation.savings.SavingsViewModel
import com.budgetquest.presentation.transaction.TransactionViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Shared Koin DI module — used by both Android and iOS.
 * All repositories use local implementations.
 * To integrate backend: swap Local*Repository with Synced*Repository.
 */
val sharedModule = module {
    // Repositories (local-only — swap for remote-synced implementations later)
    single<TransactionRepository> { LocalTransactionRepository() }
    single<BudgetRepository> { LocalBudgetRepository() }
    single<SavingsGoalRepository> { LocalSavingsGoalRepository() }
    single<UserRepository> { LocalUserRepository() }
    single<QuestRepository> { LocalQuestRepository() }

    // Use Cases
    singleOf(::AddTransactionUseCase)
    singleOf(::CalculateBudgetUseCase)
    singleOf(::TrackStreakUseCase)
    singleOf(::GetQuestsUseCase)

    // ViewModels
    factory {
        DashboardViewModel(
            calculateBudget = get(),
            getQuests = get(),
            trackStreak = get(),
            userRepo = get(),
            savingsRepo = get(),
            transactionRepo = get()
        )
    }
    factory {
        TransactionViewModel(
            addTransaction = get(),
            trackStreak = get()
        )
    }
    factory {
        BudgetViewModel(
            calculateBudget = get(),
            budgetRepo = get()
        )
    }
    factory {
        SavingsViewModel(
            savingsRepo = get(),
            userRepo = get()
        )
    }
    factory {
        OnboardingViewModel(
            userRepo = get()
        )
    }
}
