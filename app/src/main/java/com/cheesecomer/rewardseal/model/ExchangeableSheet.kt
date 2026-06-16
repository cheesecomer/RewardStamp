package com.cheesecomer.rewardseal.model

data class ExchangeableSheet(
    val id: Long,
    val title: String,
    val unconsumedCompletedCount: Int,
    val exchangeableMilestones: List<RewardMilestone>,
    val nextMilestone: RewardMilestone?,
)
