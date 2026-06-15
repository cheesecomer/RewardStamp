package com.cheesecomer.rewardseal.ui.screen.sheetedit

import com.cheesecomer.rewardseal.data.source.database.entity.CompletedRewardSheetEntity
import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import com.cheesecomer.rewardseal.model.RewardMilestone


fun RewardMilestone.toUiState(): RewardMilestoneForm {
    return RewardMilestoneForm(
        id = id,
        requiredCompletions = requiredCompletions.toString(),
        reward = reward,
    )
}

data class RewardMilestoneForm(
    val id: Long = 0,
    val requiredCompletions: String = "",
    val reward: String = "",
)

data class SheetEditUiState(
    val sheetId: Long = 0,
    val title: String = "",
    val goalCount: Int = 10,
    val milestones: List<RewardMilestoneForm> = listOf(
        RewardMilestoneForm(
            requiredCompletions = "1",
        ),
    ),
) {
    fun hasReward(): Boolean = milestones.all { it.reward.isNotBlank() }
}
