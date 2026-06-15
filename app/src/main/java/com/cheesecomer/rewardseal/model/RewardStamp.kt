package com.cheesecomer.rewardseal.model

import java.time.LocalDateTime

data class RewardStamp(
    val id: Long,
    val sheetId: Long,
    val completedRewardSheetId: Long?,
    val position: Int,
    val stampType: StampType,
    val stampedAt: LocalDateTime)
