package com.cheesecomer.rewardstamp.feature.sheet.list

import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.model.RewardStamp

data class SheetListUiState(
    val sheets: List<RewardSheet> = emptyList(),
    val latestStamps: Map<Long, RewardStamp> = emptyMap(),
)
