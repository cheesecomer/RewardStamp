package com.cheesecomer.rewardstamp.feature.completedsheet.list

import com.cheesecomer.rewardstamp.model.CompletedRewardSheet

data class CompletedSheetListUiState(
    val sheets: List<CompletedRewardSheet> = emptyList(),
)
