package com.budgetquest.android.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object BQColor {
    // Core Palette - High Vibrancy
    val ElectricPurple = Color(0xFF8B5CF6)
    val ElectricPurpleLight = Color(0xFFA78BFA)
    val DeepPurple = Color(0xFF4C1D95)

    val EmeraldGreen = Color(0xFF10B981)
    val EmeraldGreenLight = Color(0xFF34D399)
    val DeepGreen = Color(0xFF064E3B)

    val AmberGold = Color(0xFFF59E0B)
    val AmberGoldLight = Color(0xFFFBBF24)
    val DeepGold = Color(0xFF78350F)

    val CrimsonRed = Color(0xFFEF4444)
    val CrimsonRedLight = Color(0xFFF87171)
    val DeepRed = Color(0xFF7F1D1D)

    val InfoBlue = Color(0xFF3B82F6)
    val InfoBlueLight = Color(0xFF60A5FA)

    // Neutral / Surface Palette (Deep Dark Mode)
    val BgDark = Color(0xFF09090B)           // Zinc 950 base
    val SurfaceDark = Color(0xFF18181B)      // Zinc 900
    val SurfaceVariantDark = Color(0xFF27272A) // Zinc 800
    val BorderDark = Color(0xFF3F3F46)       // Zinc 700

    val BgLight = Color(0xFFFAFAFA)
    val SurfaceLight = Color(0xFFFFFFFF)
    val BorderLight = Color(0xFFE4E4E7)

    // Glassmorphism tokens
    val GlassSurfaceDark = Color(0x1AFFFFFF) // Very subtle white overlay
    val GlassBorderDark = Color(0x33FFFFFF)  // Subtle border highlight

    // Semantic Gradients
    val PrimaryGradient = Brush.verticalGradient(
        colors = listOf(ElectricPurple, DeepPurple)
    )
    val SuccessGradient = Brush.verticalGradient(
        colors = listOf(EmeraldGreen, DeepGreen)
    )
    val GoldGradient = Brush.verticalGradient(
        colors = listOf(AmberGoldLight, AmberGold)
    )
    val DangerGradient = Brush.verticalGradient(
        colors = listOf(CrimsonRed, DeepRed)
    )
    
    val CardOverlayGradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color(0x4D000000))
    )
}
