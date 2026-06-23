package com.cheesecomer.rewardstamp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val SheetBackground = Color(0xFFFFF8EF)
val SheetCard = Color(0xFFFFFCF8)

val SheetPrimary = Color(0xFFF85A5A)
val SheetPrimaryContainer = Color(0xFFFFE9E5)

val SheetHeading = Color(0xFF452A1A)
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
