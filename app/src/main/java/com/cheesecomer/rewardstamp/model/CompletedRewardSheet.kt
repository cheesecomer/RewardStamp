package com.cheesecomer.rewardstamp.model

import java.time.LocalDateTime

data class CompletedRewardSheet(
    val id: Long,
    val sheetId: Long,
    val title: String,
    val goalCount: Int,
    val goalStampType: GoalStampType,
    val completedAt: LocalDateTime,
    val consumedAt: LocalDateTime?,
)
