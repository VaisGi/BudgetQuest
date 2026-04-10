package com.budgetquest.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.budgetquest.android.ui.screens.*

@Composable
fun BudgetQuestNavHost(
    isOnboarded: Boolean,
    isLoggedIn: Boolean = false,
    onCompleteOnboarding: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = if (!isOnboarded) Onboarding else if (!isLoggedIn) Login else Dashboard,
        modifier = modifier
    ) {
        composable<Onboarding> {
            OnboardingScreen(
                onComplete = {
                    onCompleteOnboarding()
                    navController.navigate(Login) {
                        popUpTo(Onboarding) { inclusive = true }
                    }
                }
            )
        }

        composable<Login> {
            LoginScreen(
                onNavigateToOtp = { identifier ->
                    navController.navigate(OtpVerification(emailOrPhone = identifier))
                }
            )
        }

        composable<OtpVerification> { backStackEntry ->
            val args = backStackEntry.toRoute<OtpVerification>()
            OtpVerificationScreen(
                emailOrPhone = args.emailOrPhone,
                onLoginSuccess = {
                    navController.navigate(Dashboard) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
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
