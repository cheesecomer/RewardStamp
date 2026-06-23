package com.cheesecomer.rewardstamp.ui

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cheesecomer.rewardstamp.ui.theme.SheetBorder
import com.cheesecomer.rewardstamp.ui.theme.SheetPrimary
import com.cheesecomer.rewardstamp.ui.theme.SheetText

object RewardStampTextFieldDefaults {
    @Composable
    fun colors() =
        OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = SheetBorder,
            unfocusedBorderColor = SheetBorder,
            focusedLabelColor = SheetText,
            unfocusedLabelColor = SheetText,
            focusedTextColor = SheetText,
            unfocusedTextColor = SheetText,
            cursorColor = SheetPrimary,
        )
}
