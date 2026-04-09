package com.budgetquest.android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.budgetquest.android.ui.theme.BQColor

data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: ImageVector)

val topLevelRoutes = listOf(
    TopLevelRoute("Dashboard", Dashboard, Icons.Default.Home),
    TopLevelRoute("Budgets", Budgets, Icons.Default.PieChart),
    TopLevelRoute("Savings", Savings, Icons.Default.Savings),
    TopLevelRoute("Quests", Quests, Icons.Default.EmojiEvents)
)

@Composable
fun MainAppShell(
    isOnboarded: Boolean,
    onCompleteOnboarding: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Only show bottom bar for top level routes
    val showBottomBar = topLevelRoutes.any { topLevelRoute ->
        currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = BQColor.SurfaceDark,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    topLevelRoutes.forEach { topLevelRoute ->
                        val isSelected = currentDestination?.hierarchy?.any {
                            it.hasRoute(topLevelRoute.route::class)
                        } == true
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(topLevelRoute.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    popUpTo(Dashboard) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            },
                            icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.name) },
                            label = { Text(topLevelRoute.name) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = BQColor.ElectricPurple.copy(alpha = 0.2f),
                                selectedIconColor = BQColor.ElectricPurpleLight,
                                selectedTextColor = BQColor.ElectricPurpleLight,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        BudgetQuestNavHost(
            isOnboarded = isOnboarded,
            onCompleteOnboarding = onCompleteOnboarding,
            navController = navController,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}
