package com.budgetquest.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.budgetquest.android.ui.screens.*

object Routes {
    const val ONBOARDING = "onboarding"
    const val DASHBOARD = "dashboard"
    const val ADD_TRANSACTION = "add_transaction"
    const val BUDGETS = "budgets"
    const val SAVINGS = "savings"
    const val QUESTS = "quests"
}

@Composable
fun BudgetQuestNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onAddTransaction = { navController.navigate(Routes.ADD_TRANSACTION) },
                onViewBudgets = { navController.navigate(Routes.BUDGETS) },
                onViewSavings = { navController.navigate(Routes.SAVINGS) },
                onViewQuests = { navController.navigate(Routes.QUESTS) }
            )
        }

        composable(Routes.ADD_TRANSACTION) {
            TransactionScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.BUDGETS) {
            BudgetScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SAVINGS) {
            SavingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.QUESTS) {
            QuestScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
