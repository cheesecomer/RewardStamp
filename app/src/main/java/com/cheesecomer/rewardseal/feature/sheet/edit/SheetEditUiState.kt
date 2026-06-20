package com.cheesecomer.rewardseal.feature.sheet.edit

import com.cheesecomer.rewardseal.model.RewardMilestone

data class SheetEditUiState(
    val sheetId: Long = 0,
    val title: String = "",
    val goalCount: Int = 10,
    val milestones: List<RewardMilestone> = emptyList(),
) {
    fun canSave(): Boolean = title.isNotBlank() && milestones.isNotEmpty()
}
