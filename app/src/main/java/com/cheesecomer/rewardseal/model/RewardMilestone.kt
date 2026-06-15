package com.cheesecomer.rewardseal.model

data class RewardMilestone(
    val id: Long,
    val sheetId: Long,
    val requiredCompletions: Int,
    val reward: String,)
