package com.budgetquest.android.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.budgetquest.android.ui.theme.BQColor
import com.budgetquest.android.ui.theme.BQShapes
import com.budgetquest.android.ui.theme.BQTypography

@Composable
fun BQGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = BQShapes.CardPremium,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(BQColor.GlassBorderDark, Color.Transparent)
                ),
                shape = shape
            ),
        color = BQColor.GlassSurfaceDark,
        shape = shape,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun BQProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = BQColor.ElectricPurple,
    trackColor: Color = BQColor.SurfaceVariantDark
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "progress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(CircleShape)
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(color.copy(alpha = 0.7f), color)
                    )
                )
        )
    }
}

@Composable
fun BQStreakBadge(
    days: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(BQColor.GoldGradient)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "🔥",
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$days DAY STREAK",
            style = BQTypography.LabelMedium,
            color = BQColor.DeepGold,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BQXPBadge(
    xp: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = BQColor.ElectricPurple.copy(alpha = 0.15f),
        shape = CircleShape,
        border = BorderStroke(1.dp, BQColor.ElectricPurple.copy(alpha = 0.3f))
    ) {
        Text(
            text = "$xp XP",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = BQTypography.LabelSmall,
            color = BQColor.ElectricPurpleLight,
            fontWeight = FontWeight.Bold
        )
    }
}
