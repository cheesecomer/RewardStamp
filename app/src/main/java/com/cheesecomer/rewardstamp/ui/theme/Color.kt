package com.cheesecomer.rewardstamp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

val SheetBackground = Color(0xFFFFF8EF)
val SheetCard = Color(0xFFFFFCF8)

val SheetPrimary = Color(0xFFF85A5A)
val SheetPrimaryContainer = Color(0xFFFFE9E5)

val SheetText = Color(0xFF5A3925)

val SheetDivider = Color(0xFFF2D8CF)
val SheetBorder = Color(0xFFF6CFC5)

val GoalOuter = SheetBorder
val GoalInner = Color(0xFFE8B8AC)
val StampInk = Color(0xFFC53A32)

val SheetSection =
    lerp(
        SheetText,
        SheetPrimary,
        0.5f,
    )
