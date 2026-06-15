package com.cheesecomer.rewardseal.ui.screen.completedrewarddetail

import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import com.cheesecomer.rewardseal.model.RewardStamp

data class CompletedRewardDetailUiState (
    val reward: CompletedRewardSheet? = null,
    val stamps: List<RewardStamp> = emptyList(),
)