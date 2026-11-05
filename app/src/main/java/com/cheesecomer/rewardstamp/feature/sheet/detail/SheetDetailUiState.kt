package com.cheesecomer.rewardstamp.feature.sheet.detail

import com.cheesecomer.rewardstamp.model.GoalStampType
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.model.RewardStamp

data class SheetDetailUiState(
    val sheet: RewardSheet? = null,
    val goalStampType: GoalStampType? = null,
    val stamps: List<RewardStamp> = emptyList(),
    // Position stamped by the latest increment. Null after loading, so reopening a sheet plays no animation.
    val stampedPosition: Int? = null,
    val exchangeableRewards: List<RewardMilestone> = emptyList(),
    val lockedRewards: List<RewardMilestone> = emptyList(),
)
