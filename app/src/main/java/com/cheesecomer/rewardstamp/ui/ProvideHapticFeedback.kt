package com.cheesecomer.rewardstamp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

private object NoHapticFeedback : HapticFeedback {
    override fun performHapticFeedback(hapticFeedbackType: HapticFeedbackType) = Unit
}

// Turns off the haptic feedback of everything inside when the vibration setting is off
@Composable
fun ProvideHapticFeedback(
    enabled: Boolean,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalHapticFeedback provides if (enabled) LocalHapticFeedback.current else NoHapticFeedback,
        content = content,
    )
}
