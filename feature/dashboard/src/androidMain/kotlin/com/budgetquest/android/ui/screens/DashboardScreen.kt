package com.budgetquest.android.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.budgetquest.android.ui.theme.*
import com.budgetquest.domain.model.BudgetStatus
import com.budgetquest.presentation.dashboard.*
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAddTransaction: () -> Unit,
    onViewBudgets: () -> Unit,
    onViewSavings: () -> Unit,
    onViewQuests: () -> Unit,
    viewModel: DashboardViewModel = koinInject()
) {
    val config by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(DashboardEvent.OnScreenLoaded)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransaction,
                containerColor = PrimaryGreen,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Greeting + Level Header
            item {
                GreetingHeader(
                    greeting = config.greeting,
                    level = config.currentLevel,
                    xpProgress = config.xpProgress,
                    totalXp = config.totalXp
                )
            }

            // Streak Card
            item {
                StreakCard(
                    streakDays = config.streakDays,
                    isVisible = config.streakBadgeVisible
                )
            }

            // Spending Overview
            item {
                SpendingOverviewCard(
                    todaySpent = config.todaySpent,
                    monthlySpent = config.monthlySpent,
                    monthlyBudget = config.monthlyBudgetTotal
                )
            }

            // Active Quests
            if (config.activeQuests.isNotEmpty()) {
                item {
                    SectionHeader("Active Quests ⚔️", onViewAll = onViewQuests)
                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(config.activeQuests) { quest ->
                            QuestMiniCard(quest)
                        }
                    }
                }
            }

            // Budget Summary
            if (config.budgetCards.isNotEmpty()) {
                item {
                    SectionHeader("Budget Snapshot 📊", onViewAll = onViewBudgets)
                }
                items(config.budgetCards) { budget ->
                    BudgetMiniCard(budget)
                }
            }

            // Savings Goal
            if (config.hasSavingsGoal) {
                item {
                    SectionHeader("Savings Goal ${config.savingsGoalEmoji}", onViewAll = onViewSavings)
                }
                item {
                    SavingsProgressCard(
                        name = config.savingsGoalName,
                        emoji = config.savingsGoalEmoji,
                        progress = config.savingsGoalProgress
                    )
                }
            }

            // Premium Upgrade
            if (config.showUpgradePrompt) {
                item {
                    PremiumBanner()
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun GreetingHeader(greeting: String, level: Int, xpProgress: Float, totalXp: Int) {
    Column {
        Text(
            text = greeting,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Level badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = AccentPurple.copy(alpha = 0.15f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = AccentPurple,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "Level $level",
                        style = MaterialTheme.typography.labelLarge,
                        color = AccentPurple
                    )
                }
            }

            // XP Progress
            Column(modifier = Modifier.weight(1f)) {
                LinearProgressIndicator(
                    progress = { xpProgress },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = AccentPurple,
                    trackColor = AccentPurple.copy(alpha = 0.15f),
                )
                Text(
                    "$totalXp XP",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StreakCard(streakDays: Int, isVisible: Boolean) {
    AnimatedVisibility(visible = isVisible, enter = fadeIn() + slideInVertically()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = AccentGold.copy(alpha = 0.12f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("🔥", fontSize = 32.sp)
                Column {
                    Text(
                        "$streakDays Day Streak!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AccentOrange
                    )
                    Text(
                        "Keep logging to maintain your streak",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun SpendingOverviewCard(todaySpent: Double, monthlySpent: Double, monthlyBudget: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Today's Spending",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "$${String.format("%.2f", todaySpent)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Monthly", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        "$${String.format("%.0f", monthlySpent)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Budget", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        "$${String.format("%.0f", monthlyBudget)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, onViewAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = onViewAll) {
            Text("View All", color = AccentPurple)
        }
    }
}

@Composable
private fun QuestMiniCard(quest: QuestCardConfig) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(quest.emoji, fontSize = 24.sp)
                Text(
                    quest.title,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { quest.progress },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = if (quest.isCompleted) PrimaryGreen else AccentPurple,
                trackColor = AccentPurple.copy(alpha = 0.15f),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "+${quest.xpReward} XP",
                style = MaterialTheme.typography.bodySmall,
                color = AccentGold
            )
        }
    }
}

@Composable
private fun BudgetMiniCard(budget: BudgetCardConfig) {
    val statusColor = when (budget.status) {
        BudgetStatus.HEALTHY -> PrimaryGreen
        BudgetStatus.MODERATE -> AccentGold
        BudgetStatus.WARNING -> AccentOrange
        BudgetStatus.OVER -> AccentRed
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(budget.categoryEmoji, fontSize = 28.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(budget.categoryName, style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { budget.percentUsed },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = statusColor,
                    trackColor = statusColor.copy(alpha = 0.15f),
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$${String.format("%.0f", budget.spent)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "of $${String.format("%.0f", budget.limit)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SavingsProgressCard(name: String, emoji: String, progress: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryGreen.copy(alpha = 0.08f)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(emoji, fontSize = 28.sp)
                Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                color = PrimaryGreen,
                trackColor = PrimaryGreen.copy(alpha = 0.15f),
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "${(progress * 100).toInt()}% saved",
                style = MaterialTheme.typography.bodySmall,
                color = PrimaryGreen
            )
        }
    }
}

@Composable
private fun PremiumBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(AccentPurple, AccentPurpleDark)
                    ),
                    RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Text("⭐ Unlock Premium", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                Text(
                    "Unlimited goals, bank sync, AI insights",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGold, contentColor = Color.Black)
                ) {
                    Text("Start Free Trial", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
