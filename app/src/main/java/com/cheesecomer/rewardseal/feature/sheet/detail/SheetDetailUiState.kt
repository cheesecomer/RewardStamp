package com.cheesecomer.rewardseal.feature.sheet.detail

import com.cheesecomer.rewardseal.model.GoalStampType
import com.cheesecomer.rewardseal.model.RewardMilestone
import com.cheesecomer.rewardseal.model.RewardSheet
import com.cheesecomer.rewardseal.model.RewardStamp

data class SheetDetailUiState(
    val sheet: RewardSheet? = null,
    val goalStampType: GoalStampType? = null,
    val stamps: List<RewardStamp> = emptyList(),
    val exchangeableRewards: List<RewardMilestone> = emptyList(),
    val lockedRewards: List<RewardMilestone> = emptyList(),
)
