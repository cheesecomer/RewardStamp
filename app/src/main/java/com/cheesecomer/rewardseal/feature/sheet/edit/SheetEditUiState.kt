package com.cheesecomer.rewardseal.feature.sheet.edit

import com.cheesecomer.rewardseal.model.RewardMilestone

fun RewardMilestone.toUiState(): RewardMilestoneUiState =
    RewardMilestoneUiState(
        id = id,
        requiredCompletions = requiredCompletions.toString(),
        reward = reward,
    )

data class RewardMilestoneUiState(
    val id: Long = 0,
    val requiredCompletions: String = "",
    val reward: String = "",
) {
    fun isValid() =
        reward.isNotBlank() &&
            (requiredCompletions.toIntOrNull() ?: 0) > 0
}

data class SheetEditUiState(
    val sheetId: Long = 0,
    val title: String = "",
    val goalCount: Int = 10,
    val milestones: List<RewardMilestoneUiState> = emptyList(),
) {
    fun canSave(): Boolean = title.isNotBlank() && milestones.isNotEmpty() && milestones.all { it.isValid() }
}
