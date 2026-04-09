package com.budgetquest.android.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object BQShapes {
    val CornerNone = RoundedCornerShape(0.dp)
    val CornerSmall = RoundedCornerShape(4.dp)
    val CornerMedium = RoundedCornerShape(8.dp)
    val CornerLarge = RoundedCornerShape(16.dp)
    val CornerExtraLarge = RoundedCornerShape(24.dp)
    val CornerFull = RoundedCornerShape(100) // Generic Capsule/Circle

    // Semantic shapes
    val CardDefault = CornerLarge
    val CardPremium = CornerExtraLarge
    val ProgressTrack = CornerFull
}

val DefaultShapes = Shapes(
    small = BQShapes.CornerSmall,
    medium = BQShapes.CornerMedium,
    large = BQShapes.CornerLarge,
    extraLarge = BQShapes.CornerExtraLarge
)
