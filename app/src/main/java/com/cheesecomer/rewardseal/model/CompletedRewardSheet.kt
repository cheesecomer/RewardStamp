package com.cheesecomer.rewardseal.model

import java.time.LocalDateTime

data class CompletedRewardSheet(
    val id: Long,
    val sheetId: Long,
    val title: String,
    val goalCount: Int,
    val completedAt: LocalDateTime,
    val consumedAt: LocalDateTime?,
)