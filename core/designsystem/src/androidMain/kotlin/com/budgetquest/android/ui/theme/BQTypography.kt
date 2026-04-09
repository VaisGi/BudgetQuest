package com.budgetquest.android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object BQTypography {
    val DisplayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = (-0.5).sp
    )

    val DisplayMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = (-0.2).sp
    )

    val TitleBold = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 0.sp
    )

    val BodyRegular = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    )

    val LabelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    )

    val LabelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        letterSpacing = 0.5.sp
    )
}

val DefaultTypography = Typography(
    displayLarge = BQTypography.DisplayLarge,
    displayMedium = BQTypography.DisplayMedium,
    headlineLarge = BQTypography.DisplayLarge.copy(fontSize = 28.sp),
    titleLarge = BQTypography.TitleBold,
    titleMedium = BQTypography.TitleBold.copy(fontSize = 16.sp, fontWeight = FontWeight.Medium),
    bodyLarge = BQTypography.BodyRegular.copy(fontSize = 16.sp),
    bodyMedium = BQTypography.BodyRegular,
    bodySmall = BQTypography.BodyRegular.copy(fontSize = 12.sp),
    labelLarge = BQTypography.LabelMedium,
    labelMedium = BQTypography.LabelSmall
)
