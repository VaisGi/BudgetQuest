package com.budgetquest.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.budgetquest.presentation.dashboard.QuestCardConfig
import com.budgetquest.domain.usecase.GetQuestsUseCase
import com.budgetquest.domain.repository.QuestRepository
import kotlinx.coroutines.flow.first
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestScreen(
    onBack: () -> Unit,
    questRepo: QuestRepository = koinInject()
) {
    var activeQuests by remember { mutableStateOf<List<QuestCardConfig>>(emptyList()) }

    LaunchedEffect(Unit) {
        questRepo.getActiveQuests().collect { quests ->
            activeQuests = quests.map { q ->
                QuestCardConfig(
                    id = q.id,
                    title = q.title,
                    emoji = q.emoji,
                    progress = q.progress,
                    xpReward = q.xpReward,
                    isCompleted = q.isCompleted
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quests ⚔️") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            if (activeQuests.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⚔️", fontSize = 48.sp)
                            Spacer(Modifier.height(16.dp))
                            Text("No active quests", style = BQTypography.TitleBold)
                            Text(
                                "New quests appear weekly",
                                style = BQTypography.BodyRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            items(activeQuests) { quest ->
                QuestCard(quest)
            }
        }
    }
}

@Composable
private fun QuestCard(quest: QuestCardConfig) {
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
                Text(quest.emoji, fontSize = 32.sp)
                Text(
                    quest.title,
                    style = BQTypography.TitleBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            BQXPBadge(xp = quest.xpReward)
        }
        Spacer(Modifier.height(16.dp))
        BQProgressBar(
            progress = quest.progress,
            color = if (quest.isCompleted) BQColor.EmeraldGreen else BQColor.ElectricPurple
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (quest.isCompleted) "✅ Completed!" else "${(quest.progress * 100).toInt()}% complete",
            style = BQTypography.LabelSmall,
            color = if (quest.isCompleted) BQColor.EmeraldGreen else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
