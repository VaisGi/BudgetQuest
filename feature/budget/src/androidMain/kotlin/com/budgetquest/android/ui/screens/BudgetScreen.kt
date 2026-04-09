package com.budgetquest.android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.budgetquest.android.ui.theme.*
import com.budgetquest.android.ui.components.*
import com.budgetquest.domain.model.BudgetStatus
import com.budgetquest.presentation.budget.*
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    onBack: () -> Unit,
    viewModel: BudgetViewModel = koinInject()
) {
    val config by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(BudgetEvent.OnScreenLoaded)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Budgets 📊") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(BudgetEvent.OnAddBudgetTapped) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Budget")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Overall Progress
            item {
                BQGlassCard {
                    Text("Monthly Overview", style = BQTypography.TitleBold, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(Modifier.height(12.dp))
                    BQProgressBar(
                        progress = config.overallPercent,
                        color = when {
                            config.overallPercent > 0.8f -> BQColor.CrimsonRed
                            config.overallPercent > 0.5f -> BQColor.AmberGold
                            else -> BQColor.EmeraldGreen
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "$${String.format("%.0f", config.totalSpent)} spent",
                            style = BQTypography.LabelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "$${String.format("%.0f", config.totalLimit)} budget",
                            style = BQTypography.LabelMedium,
                            color = BQColor.EmeraldGreenLight,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Budget Items
            items(config.budgets) { budget ->
                BudgetItemCard(budget)
            }

            if (config.budgets.isEmpty() && !config.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📊", fontSize = 48.sp)
                            Spacer(Modifier.height(16.dp))
                            Text("No budgets yet", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Tap + to create your first budget",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BudgetItemCard(budget: BudgetItemConfig) {
    val statusColor = when (budget.status) {
        BudgetStatus.HEALTHY -> BQColor.EmeraldGreen
        BudgetStatus.MODERATE -> BQColor.AmberGold
        BudgetStatus.WARNING -> BQColor.AmberGoldLight
        BudgetStatus.OVER -> BQColor.CrimsonRed
    }

    BQGlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
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
                Column {
                    Text(budget.categoryName, style = BQTypography.TitleBold, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        "$${String.format("%.0f", budget.remaining)} remaining",
                        style = BQTypography.LabelSmall,
                        color = statusColor
                    )
                }
            }
            Text(
                "$${String.format("%.0f", budget.spent)} / $${String.format("%.0f", budget.limit)}",
                style = BQTypography.TitleBold,
                color = statusColor
            )
        }
        Spacer(Modifier.height(16.dp))
        BQProgressBar(
            progress = budget.percentUsed,
            color = statusColor
        )
    }
}
