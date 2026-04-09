package com.budgetquest.di

import com.budgetquest.core.database.DatabaseDriverFactory
import com.budgetquest.core.database.createBudgetQuestDatabase
import com.budgetquest.data.local.*
import com.budgetquest.db.BudgetQuestDatabase
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
 * Shared Koin DI module.
 *
 * Pass a [DatabaseDriverFactory] (platform-specific) when calling [createSharedModule].
 * This creates the real SQLDelight DB and wires all repositories.
 */
fun createSharedModule(driverFactory: DatabaseDriverFactory) = module {

    // ── Database ──────────────────────────────────────────────────────────────
    single { createBudgetQuestDatabase(driverFactory) }
    single { get<BudgetQuestDatabase>().budgetQuestDatabaseQueries }

    // ── Repositories (SQLDelight-backed) ──────────────────────────────────────
    single<TransactionRepository> { SqlDelightTransactionRepository(get()) }
    single<BudgetRepository>      { SqlDelightBudgetRepository(get())      }
    single<SavingsGoalRepository> { SqlDelightSavingsGoalRepository(get()) }
    single<UserRepository>        { SqlDelightUserRepository(get())        }
    single<QuestRepository>       { SqlDelightQuestRepository(get())       }

    // ── Use Cases ─────────────────────────────────────────────────────────────
    singleOf(::AddTransactionUseCase)
    singleOf(::CalculateBudgetUseCase)
    singleOf(::TrackStreakUseCase)
    singleOf(::GetQuestsUseCase)

    // ── ViewModels ────────────────────────────────────────────────────────────
    factory {
        DashboardViewModel(
            calculateBudget = get(),
            getQuests       = get(),
            trackStreak     = get(),
            userRepo        = get(),
            savingsRepo     = get(),
            transactionRepo = get()
        )
    }
    factory {
        TransactionViewModel(
            addTransaction = get(),
            trackStreak    = get()
        )
    }
    factory {
        BudgetViewModel(
            calculateBudget = get(),
            budgetRepo      = get()
        )
    }
    factory {
        SavingsViewModel(
            savingsRepo = get(),
            userRepo    = get()
        )
    }
    factory {
        OnboardingViewModel(
            userRepo = get()
        )
    }
}

// Keep a backward-compat alias so existing call sites that use `sharedModule` still compile
// (will be cleaned up once all call sites migrate to createSharedModule)
val sharedModule get() = createSharedModule(
    throw IllegalStateException("Use createSharedModule(driverFactory) instead of sharedModule directly")
)
