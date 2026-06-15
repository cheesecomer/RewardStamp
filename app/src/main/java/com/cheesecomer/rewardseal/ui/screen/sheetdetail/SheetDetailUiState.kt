package com.cheesecomer.rewardseal.ui.screen.sheetdetail

import com.cheesecomer.rewardseal.model.RewardMilestone
import com.cheesecomer.rewardseal.model.RewardSheet
import com.cheesecomer.rewardseal.model.RewardStamp

data class SheetDetailUiState(
    val sheet: RewardSheet? = null,
    val stamps: List<RewardStamp> = emptyList(),
)
