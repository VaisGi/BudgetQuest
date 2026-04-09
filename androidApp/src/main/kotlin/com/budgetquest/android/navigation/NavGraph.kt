package com.budgetquest.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.budgetquest.android.ui.screens.*

@Composable
fun BudgetQuestNavHost(
    isOnboarded: Boolean,
    onCompleteOnboarding: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = if (isOnboarded) Dashboard else Onboarding,
        modifier = modifier
    ) {
        composable<Onboarding> {
            OnboardingScreen(
                onComplete = {
                    onCompleteOnboarding()
                    navController.navigate(Dashboard) {
                        popUpTo(Onboarding) { inclusive = true }
                    }
                }
            )
        }

        composable<Dashboard> {
            DashboardScreen(
                onAddTransaction = { navController.navigate(AddTransaction) },
                onViewBudgets = { navController.navigate(Budgets) },
                onViewSavings = { navController.navigate(Savings) },
                onViewQuests = { navController.navigate(Quests) }
            )
        }

        composable<AddTransaction> {
            TransactionScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Budgets> {
            BudgetScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Savings> {
            SavingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Quests> {
            QuestScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
