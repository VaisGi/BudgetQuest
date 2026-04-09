package com.budgetquest.android.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
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
import com.budgetquest.android.ui.components.*
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
                containerColor = BQColor.ElectricPurple,
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        },
        containerColor = Color.Transparent // Allow background gradient or solid zinc
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp)
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
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = greeting,
            style = BQTypography.DisplayMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Level badge
            Surface(
                shape = CircleShape,
                color = BQColor.ElectricPurple.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, BQColor.ElectricPurple.copy(alpha = 0.2f))
            ) {
                Text(
                    text = "LEVEL $level",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = BQTypography.LabelMedium,
                    color = BQColor.ElectricPurpleLight,
                    fontWeight = FontWeight.Bold
                )
            }

            // XP Progress
            Column(modifier = Modifier.weight(1f)) {
                BQProgressBar(
                    progress = xpProgress,
                    color = BQColor.ElectricPurple
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "$totalXp XP",
                        style = BQTypography.LabelSmall,
                        color = BQColor.ElectricPurpleLight
                    )
                    Text(
                        "NEXT LEVEL",
                        style = BQTypography.LabelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StreakCard(streakDays: Int, isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible, 
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        BQGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = BQShapes.CardPremium
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    BQStreakBadge(days = streakDays)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "You're on fire! Keep logging your expenses to grow your streak and earn bonus XP.",
                        style = BQTypography.BodyRegular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "🏆",
                    fontSize = 44.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun SpendingOverviewCard(todaySpent: Double, monthlySpent: Double, monthlyBudget: Double) {
    BQGlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            "Today's Spending",
            style = BQTypography.LabelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            "$${String.format("%.2f", todaySpent)}",
            style = BQTypography.DisplayLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("MONTHLY", style = BQTypography.LabelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    "$${String.format("%.0f", monthlySpent)}",
                    style = BQTypography.TitleBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("BUDGET", style = BQTypography.LabelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    "$${String.format("%.0f", monthlyBudget)}",
                    style = BQTypography.TitleBold,
                    color = BQColor.EmeraldGreenLight
                )
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
            style = BQTypography.TitleBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        TextButton(
            onClick = onViewAll,
            colors = ButtonDefaults.textButtonColors(contentColor = BQColor.ElectricPurpleLight)
        ) {
            Text("View All", style = BQTypography.LabelMedium)
        }
    }
}

@Composable
private fun QuestMiniCard(quest: QuestCardConfig) {
    BQGlassCard(
        modifier = Modifier.width(220.dp),
        shape = BQShapes.CardDefault
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(quest.emoji, fontSize = 28.sp)
            Text(
                quest.title,
                style = BQTypography.TitleBold.copy(fontSize = 14.sp),
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.height(12.dp))
        BQProgressBar(
            progress = quest.progress,
            color = if (quest.isCompleted) BQColor.EmeraldGreen else BQColor.ElectricPurple
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BQXPBadge(xp = quest.xpReward)
            if (quest.isCompleted) {
                Text(
                    "COMPLETED",
                    style = BQTypography.LabelSmall,
                    color = BQColor.EmeraldGreenLight,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun BudgetMiniCard(budget: BudgetCardConfig) {
    val statusColor = when (budget.status) {
        BudgetStatus.HEALTHY -> BQColor.EmeraldGreen
        BudgetStatus.MODERATE -> BQColor.AmberGold
        BudgetStatus.WARNING -> Color(0xFFF97316) // Orange
        BudgetStatus.OVER -> BQColor.CrimsonRed
    }

    BQGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = BQShapes.CardDefault
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(statusColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(budget.categoryEmoji, fontSize = 24.sp)
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(budget.categoryName, style = BQTypography.TitleBold)
                    Text(
                        "$${String.format("%.0f", budget.spent)}",
                        style = BQTypography.TitleBold,
                        color = statusColor
                    )
                }
                Spacer(Modifier.height(8.dp))
                BQProgressBar(
                    progress = budget.percentUsed,
                    color = statusColor
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Spent $${String.format("%.0f", budget.spent)} of $${String.format("%.0f", budget.limit)}",
                    style = BQTypography.LabelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SavingsProgressCard(name: String, emoji: String, progress: Float) {
    BQGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = BQShapes.CardPremium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(BQColor.EmeraldGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 32.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = BQTypography.TitleBold)
                Spacer(Modifier.height(8.dp))
                BQProgressBar(
                    progress = progress,
                    color = BQColor.EmeraldGreen
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${(progress * 100).toInt()}% towards your goal",
                    style = BQTypography.LabelSmall,
                    color = BQColor.EmeraldGreenLight,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PremiumBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = BQShapes.CardPremium,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(BQColor.ElectricPurple, BQColor.DeepPurple)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⭐", fontSize = 24.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Unlock Premium",
                        style = BQTypography.TitleBold,
                        color = Color.White
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "Unlimited goals, bank sync, and AI-powered financial insights are waiting.",
                    style = BQTypography.BodyRegular,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BQColor.AmberGold,
                        contentColor = BQColor.DeepGold
                    ),
                    shape = BQShapes.CornerMedium
                ) {
                    Text("START 7-DAY FREE TRIAL", style = BQTypography.LabelMedium, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
