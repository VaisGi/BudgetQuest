package com.budgetquest.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.budgetquest.android.ui.theme.*
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Monthly Overview", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { config.overallPercent },
                            modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)),
                            color = when {
                                config.overallPercent > 0.8f -> AccentRed
                                config.overallPercent > 0.5f -> AccentGold
                                else -> PrimaryGreen
                            },
                            trackColor = MaterialTheme.colorScheme.surface,
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "$${String.format("%.0f", config.totalSpent)} spent",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                "$${String.format("%.0f", config.totalLimit)} budget",
                                style = MaterialTheme.typography.bodyMedium,
                                color = PrimaryGreen
                            )
                        }
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
        BudgetStatus.HEALTHY -> PrimaryGreen
        BudgetStatus.MODERATE -> AccentGold
        BudgetStatus.WARNING -> AccentOrange
        BudgetStatus.OVER -> AccentRed
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(budget.categoryEmoji, fontSize = 28.sp)
                    Column {
                        Text(budget.categoryName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            "$${String.format("%.0f", budget.remaining)} remaining",
                            style = MaterialTheme.typography.bodySmall,
                            color = statusColor
                        )
                    }
                }
                Text(
                    "$${String.format("%.0f", budget.spent)} / $${String.format("%.0f", budget.limit)}",
                    style = MaterialTheme.typography.labelLarge,
                    color = statusColor
                )
            }
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { budget.percentUsed },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = statusColor,
                trackColor = statusColor.copy(alpha = 0.12f),
            )
        }
    }
}
