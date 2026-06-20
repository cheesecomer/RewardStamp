package com.cheesecomer.rewardseal.model

data class ExchangeableSheet(
    val rewardSheet: RewardSheet,
    val exchangeableSheetCount: Int,
    val exchangeableMilestones: List<RewardMilestone>,
    val closestMilestone: RewardMilestone?,
)
