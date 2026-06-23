package com.cheesecomer.rewardstamp.model

import java.time.LocalDateTime
import java.time.ZoneOffset

data class RewardStamp(
    val id: Long,
    val sheetId: Long,
    val completedRewardSheetId: Long?,
    val position: Int,
    val stampType: StampType,
    val stampedAt: LocalDateTime,
) {
    val randomSeed = this.stampedAt.toEpochSecond(ZoneOffset.ofHours(0))
}
