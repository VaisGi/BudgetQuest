package com.budgetquest.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Gamified Finance Color Palette
val PrimaryGreen = Color(0xFF00C896)       // Success, positive money actions
val PrimaryGreenDark = Color(0xFF00A67E)
val AccentPurple = Color(0xFF7C4DFF)       // XP, gamification, premium
val AccentPurpleDark = Color(0xFF6200EA)
val AccentGold = Color(0xFFFFAB00)          // Streaks, rewards, badges
val AccentOrange = Color(0xFFFF6D00)        // Warnings, over-budget
val AccentRed = Color(0xFFFF1744)           // Critical, over-spent
val AccentBlue = Color(0xFF2979FF)          // Info, links

// Dark theme surface colors
val DarkSurface = Color(0xFF121218)
val DarkSurfaceVariant = Color(0xFF1E1E2A)
val DarkCard = Color(0xFF252536)
val DarkCardElevated = Color(0xFF2D2D42)

// Light theme
val LightBackground = Color(0xFFF8F9FE)
val LightSurface = Color(0xFFFFFFFF)
val LightCard = Color(0xFFF0F2FA)

val TextPrimary = Color(0xFFE8E8F0)
val TextSecondary = Color(0xFFA0A0B8)
val TextOnDark = Color(0xFFE8E8F0)
val TextOnLight = Color(0xFF1A1A2E)
val TextOnPrimary = Color(0xFFFFFFFF)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = TextOnPrimary,
    secondary = AccentPurple,
    onSecondary = TextOnPrimary,
    tertiary = AccentGold,
    background = DarkSurface,
    surface = DarkSurfaceVariant,
    surfaceVariant = DarkCard,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error = AccentRed,
    onError = TextOnPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreenDark,
    onPrimary = TextOnPrimary,
    secondary = AccentPurpleDark,
    onSecondary = TextOnPrimary,
    tertiary = AccentGold,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightCard,
    onBackground = TextOnLight,
    onSurface = TextOnLight,
    onSurfaceVariant = Color(0xFF606080),
    error = AccentRed,
    onError = TextOnPrimary
)

val BudgetQuestTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        letterSpacing = (-0.3).sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
fun BudgetQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BudgetQuestTypography,
        content = content
    )
}
