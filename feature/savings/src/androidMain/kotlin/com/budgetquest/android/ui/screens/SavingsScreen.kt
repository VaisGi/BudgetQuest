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
import com.budgetquest.android.ui.components.*
import com.budgetquest.domain.model.SavingsMilestone
import com.budgetquest.presentation.savings.*
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsScreen(
    onBack: () -> Unit,
    viewModel: SavingsViewModel = koinInject()
) {
    val config by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onEvent(SavingsEvent.OnScreenLoaded)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Savings Goals 💰") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(SavingsEvent.OnAddGoalTapped) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Goal")
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(config.goals) { goal ->
                SavingsGoalCard(
                    goal = goal,
                    onAddSavings = { amount ->
                        viewModel.onEvent(SavingsEvent.OnAddSavings(goal.id, amount))
                    }
                )
            }

            if (config.goals.isEmpty() && !config.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎯", fontSize = 48.sp)
                            Spacer(Modifier.height(16.dp))
                            Text("No savings goals", style = BQTypography.TitleBold)
                            Text(
                                "Set a goal and start saving!",
                                style = BQTypography.BodyRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.onEvent(SavingsEvent.OnAddGoalTapped) },
                                colors = ButtonDefaults.buttonColors(containerColor = BQColor.EmeraldGreen)
                            ) {
                                Text("Create Goal")
                            }
                        }
                    }
                }
            }
        }
    }

    // Milestone celebration
    if (config.showMilestoneAnimation && config.reachedMilestone != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(SavingsEvent.OnDismissMilestone) },
            title = {
                Text(
                    "${config.reachedMilestone!!.badge} ${config.reachedMilestone!!.displayName}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Amazing progress! Keep going! 🎉")
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(SavingsEvent.OnDismissMilestone) }) {
                    Text("Awesome!", color = BQColor.EmeraldGreen)
                }
            }
        )
    }
}

@Composable
private fun SavingsGoalCard(goal: SavingsGoalItemConfig, onAddSavings: (Double) -> Unit) {
    val milestoneColor = when (goal.milestone) {
        SavingsMilestone.STARTED -> MaterialTheme.colorScheme.onSurfaceVariant
        SavingsMilestone.QUARTER -> BQColor.ElectricPurple
        SavingsMilestone.HALFWAY -> BQColor.AmberGold
        SavingsMilestone.THREE_QUARTERS -> BQColor.AmberGoldLight
        SavingsMilestone.COMPLETE -> BQColor.EmeraldGreen
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
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(goal.emoji, fontSize = 32.sp)
                Column {
                    Text(goal.name, style = BQTypography.TitleBold, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        "${goal.milestone.badge} ${goal.milestone.displayName}",
                        style = BQTypography.LabelSmall,
                        color = milestoneColor
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        BQProgressBar(
            progress = goal.progress,
            color = milestoneColor
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "$${String.format("%.0f", goal.savedAmount)} saved",
                style = BQTypography.LabelMedium,
                color = milestoneColor
            )
            Text(
                "of $${String.format("%.0f", goal.targetAmount)}",
                style = BQTypography.LabelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

            if (!goal.isCompleted) {
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(10.0, 25.0, 50.0).forEach { amount ->
                        OutlinedButton(
                            onClick = { onAddSavings(amount) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+$${amount.toInt()}", fontSize = 12.sp)
                        }
                    }
                }
            }
    }
}
