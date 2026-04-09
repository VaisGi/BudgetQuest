package com.budgetquest.android.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.budgetquest.android.ui.theme.*
import com.budgetquest.android.ui.components.*
import com.budgetquest.presentation.transaction.*
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    onBack: () -> Unit,
    viewModel: TransactionViewModel = koinInject()
) {
    val config by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(config.saveSuccess) {
        if (config.saveSuccess) {
            kotlinx.coroutines.delay(1500)
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Type Toggle (Expense / Income)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = config.isExpense,
                    onClick = { viewModel.onEvent(TransactionEvent.OnTypeToggled(true)) },
                    label = { Text("Expense") },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BQColor.CrimsonRed.copy(alpha = 0.15f),
                        selectedLabelColor = BQColor.CrimsonRed
                    )
                )
                FilterChip(
                    selected = !config.isExpense,
                    onClick = { viewModel.onEvent(TransactionEvent.OnTypeToggled(false)) },
                    label = { Text("Income") },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BQColor.EmeraldGreen.copy(alpha = 0.15f),
                        selectedLabelColor = BQColor.EmeraldGreen
                    )
                )
            }

            // Amount Input
            OutlinedTextField(
                value = config.amount,
                onValueChange = { viewModel.onEvent(TransactionEvent.OnAmountChanged(it)) },
                label = { Text("Amount", style = BQTypography.LabelSmall) },
                prefix = { Text("$", style = BQTypography.TitleBold, fontSize = 20.sp) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                textStyle = BQTypography.DisplayMedium,
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                isError = config.errorMessage != null
            )

            // Description
            OutlinedTextField(
                value = config.description,
                onValueChange = { viewModel.onEvent(TransactionEvent.OnDescriptionChanged(it)) },
                label = { Text("Description (optional)", style = BQTypography.LabelSmall) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = BQTypography.BodyRegular,
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            // Category Grid
            Text(
                "Category",
                style = BQTypography.TitleBold
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 300.dp)
            ) {
                items(config.categories) { category ->
                    CategoryChip(
                        emoji = category.emoji,
                        name = category.name,
                        isSelected = category.isSelected,
                        onClick = {
                            viewModel.onEvent(TransactionEvent.OnCategorySelected(category.category))
                        }
                    )
                }
            }

            // Error
            config.errorMessage?.let {
                Text(it, color = BQColor.CrimsonRed, style = BQTypography.LabelSmall)
            }

            Spacer(Modifier.weight(1f))

            // XP Animation
            AnimatedVisibility(
                visible = config.showXpAnimation,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut()
            ) {
                BQGlassCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("⚡", fontSize = 24.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "+${config.xpEarned} XP earned!",
                            style = BQTypography.TitleBold,
                            color = BQColor.AmberGoldLight
                        )
                    }
                }
            }

            // Save Button
            Button(
                onClick = { viewModel.onEvent(TransactionEvent.OnSaveTapped) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = config.isAmountValid && !config.isSaving,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (config.isExpense) BQColor.CrimsonRed else BQColor.EmeraldGreen
                )
            ) {
                if (config.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        if (config.saveSuccess) "✓ Saved!" else "Save Transaction",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CategoryChip(emoji: String, name: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected)
            BQColor.EmeraldGreen.copy(alpha = 0.15f)
        else
            MaterialTheme.colorScheme.surfaceVariant,
        border = if (isSelected)
            ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp)
        else null
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 24.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                name,
                style = BQTypography.LabelSmall,
                maxLines = 1,
                color = if (isSelected) BQColor.EmeraldGreen else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
