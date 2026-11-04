package com.cheesecomer.rewardstamp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorScheme =
    lightColorScheme(
        primary = SheetPrimary,
        onPrimary = Color.White,
        primaryContainer = SheetPrimaryContainer,
        onPrimaryContainer = SheetPrimary,
        secondary = SheetPrimary,
        onSecondary = Color.White,
        secondaryContainer = SheetPrimaryContainer,
        onSecondaryContainer = SheetText,
        tertiary = SheetPrimary,
        onTertiary = Color.White,
        tertiaryContainer = SheetPrimaryContainer,
        onTertiaryContainer = SheetText,
        background = SheetBackground,
        onBackground = SheetText,
        surface = SheetBackground,
        onSurface = SheetText,
        surfaceVariant = SheetPrimaryContainer,
        onSurfaceVariant = SheetText,
        surfaceContainer = SheetCard,
        surfaceContainerHigh = SheetCard,
        surfaceContainerHighest = SheetCard,
        surfaceContainerLow = SheetBackground,
        surfaceContainerLowest = SheetBackground,
        outline = SheetBorder,
        outlineVariant = SheetDivider,
        surfaceTint = SheetPrimary,
    )

@Composable
fun RewardStampTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content,
    )
}
