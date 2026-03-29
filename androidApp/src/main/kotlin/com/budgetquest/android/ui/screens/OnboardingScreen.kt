package com.budgetquest.android.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.budgetquest.android.ui.theme.*
import com.budgetquest.presentation.onboarding.*
import org.koin.compose.koinInject

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = koinInject()
) {
    val config by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(config.isComplete) {
        if (config.isComplete) onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        AccentPurple.copy(alpha = 0.05f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))

            // Progress dots
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(config.totalSteps) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == config.currentStep) 24.dp else 8.dp, 8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (index <= config.currentStep) AccentPurple
                                else AccentPurple.copy(alpha = 0.2f)
                            )
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            // Step content
            AnimatedContent(
                targetState = config.currentStep,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
                },
                modifier = Modifier.weight(1f)
            ) { step ->
                when (step) {
                    0 -> WelcomeStep()
                    1 -> GoalSelectionStep(
                        selectedGoal = config.financialGoal,
                        onGoalSelected = { viewModel.onEvent(OnboardingEvent.OnGoalSelected(it)) }
                    )
                    2 -> IncomeStep(
                        selectedIncome = config.monthlyIncome,
                        onIncomeSelected = { viewModel.onEvent(OnboardingEvent.OnIncomeSelected(it)) }
                    )
                    3 -> NameStep(
                        name = config.displayName,
                        onNameChanged = { viewModel.onEvent(OnboardingEvent.OnNameEntered(it)) }
                    )
                }
            }

            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (config.currentStep > 0) {
                    OutlinedButton(
                        onClick = { viewModel.onEvent(OnboardingEvent.OnPreviousStep) },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Back")
                    }
                } else {
                    Spacer(Modifier)
                }

                Button(
                    onClick = {
                        if (config.currentStep == config.totalSteps - 1) {
                            viewModel.onEvent(OnboardingEvent.OnComplete)
                        } else {
                            viewModel.onEvent(OnboardingEvent.OnNextStep)
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                ) {
                    Text(
                        if (config.currentStep == config.totalSteps - 1) "Let's Go! 🚀" else "Next",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun WelcomeStep() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎮", fontSize = 80.sp)
        Spacer(Modifier.height(24.dp))
        Text(
            "Welcome to\nBudgetQuest",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "Turn your finances into an adventure.\nEarn XP, complete quests, level up!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun GoalSelectionStep(
    selectedGoal: FinancialGoal?,
    onGoalSelected: (FinancialGoal) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "What's your #1\nfinancial goal?",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(32.dp))
        FinancialGoal.entries.forEach { goal ->
            GoalOption(
                goal = goal,
                isSelected = selectedGoal == goal,
                onClick = { onGoalSelected(goal) }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun GoalOption(goal: FinancialGoal, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) AccentPurple.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isSelected) ButtonDefaults.outlinedButtonBorder else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(goal.emoji, fontSize = 28.sp)
            Column {
                Text(goal.displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(goal.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun IncomeStep(
    selectedIncome: IncomeRange?,
    onIncomeSelected: (IncomeRange) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Monthly income range?",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Helps us set realistic budgets",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        IncomeRange.entries.forEach { range ->
            Surface(
                modifier = Modifier.fillMaxWidth().clickable { onIncomeSelected(range) },
                shape = RoundedCornerShape(12.dp),
                color = if (selectedIncome == range) AccentPurple.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    range.displayName,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedIncome == range) AccentPurple else MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun NameStep(name: String, onNameChanged: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("👋", fontSize = 64.sp)
        Spacer(Modifier.height(24.dp))
        Text(
            "What should we\ncall you?",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(24.dp))
        OutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            label = { Text("Your name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )
    }
}
