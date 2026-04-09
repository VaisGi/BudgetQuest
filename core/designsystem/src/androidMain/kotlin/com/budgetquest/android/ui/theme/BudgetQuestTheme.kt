package com.budgetquest.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Dark theme color scheme - Zinc 950 base
private val DarkColorScheme = darkColorScheme(
    primary = BQColor.ElectricPurple,
    onPrimary = Color.White,
    secondary = BQColor.EmeraldGreen,
    onSecondary = Color.White,
    tertiary = BQColor.AmberGold,
    background = BQColor.BgDark,
    surface = BQColor.SurfaceDark,
    surfaceVariant = BQColor.SurfaceVariantDark,
    onBackground = Color(0xFFE4E4E7),
    onSurface = Color(0xFFE4E4E7),
    onSurfaceVariant = Color(0xFFA1A1AA),
    error = BQColor.CrimsonRed,
    onError = Color.White
)

// Light theme color scheme
private val LightColorScheme = lightColorScheme(
    primary = BQColor.ElectricPurple,
    onPrimary = Color.White,
    secondary = BQColor.EmeraldGreen,
    onSecondary = Color.White,
    tertiary = BQColor.AmberGold,
    background = BQColor.BgLight,
    surface = BQColor.SurfaceLight,
    surfaceVariant = Color(0xFFF4F4F5),
    onBackground = Color(0xFF18181B),
    onSurface = Color(0xFF18181B),
    onSurfaceVariant = Color(0xFF71717A),
    error = BQColor.CrimsonRed,
    onError = Color.White
)

@Composable
fun BudgetQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DefaultTypography,
        shapes = DefaultShapes,
        content = content
    )
}
