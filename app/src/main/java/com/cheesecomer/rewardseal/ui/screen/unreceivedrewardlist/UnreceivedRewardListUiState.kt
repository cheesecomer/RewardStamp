package com.cheesecomer.rewardseal.ui.screen.unreceivedrewardlist

import com.cheesecomer.rewardseal.model.RewardMilestone

data class ExchangeableRewardListItemState(
    val id: Long,
    val title: String,
    val unconsumedCompletedCount: Int,
    val exchangeableMilestones: List<RewardMilestone>,
    val nextMilestone: RewardMilestone?
)

data class UnreceivedRewardListUiState(
    val sheets: List<ExchangeableRewardListItemState> = emptyList()
)