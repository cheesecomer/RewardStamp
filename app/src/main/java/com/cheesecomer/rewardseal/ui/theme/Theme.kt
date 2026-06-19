package com.cheesecomer.rewardseal.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = SheetPrimary,
        onPrimary = Color.White,
        primaryContainer = SheetPrimaryContainer,
        onPrimaryContainer = SheetText,
        secondary = SheetPrimary,
        onSecondary = Color.White,
        secondaryContainer = SheetPrimaryContainer,
        onSecondaryContainer = SheetText,
        background = SheetBackground,
        onBackground = SheetText,
        surface = SheetCard,
        onSurface = SheetText,
        surfaceVariant = SheetPrimaryContainer,
        onSurfaceVariant = SheetText,
        outline = SheetBorder,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = SheetPrimary,
        onPrimary = Color.White,
        primaryContainer = SheetPrimaryContainer,
        onPrimaryContainer = SheetText,
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
fun RewardSealTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
