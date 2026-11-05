package com.cheesecomer.rewardstamp.ui.component

import androidx.compose.runtime.Stable

// The stamp being pressed onto the board.
// Progress goes from 0 (not yet pressed) to 1 (pressed), and is read while drawing.
@Stable
class StampPress(
    val position: Int? = null,
    val progress: () -> Float = { 1f },
    val goalProgress: () -> Float = { 1f },
)
