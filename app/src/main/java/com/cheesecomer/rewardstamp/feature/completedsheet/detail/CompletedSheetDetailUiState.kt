package com.cheesecomer.rewardstamp.feature.completedsheet.detail

import com.cheesecomer.rewardstamp.model.CompletedRewardSheet
import com.cheesecomer.rewardstamp.model.RewardStamp

data class CompletedSheetDetailUiState(
    val sheet: CompletedRewardSheet? = null,
    val stamps: List<RewardStamp> = emptyList(),
)
