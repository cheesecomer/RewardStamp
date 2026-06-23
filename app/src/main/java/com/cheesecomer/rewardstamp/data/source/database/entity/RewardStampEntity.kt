package com.cheesecomer.rewardstamp.data.source.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "reward_stamps")
data class RewardStampEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sheetId: Long,
    val completedRewardSheetId: Long?,
    val position: Int,
    val stampTypeId: String,
    val stampedAt: LocalDateTime,
)
