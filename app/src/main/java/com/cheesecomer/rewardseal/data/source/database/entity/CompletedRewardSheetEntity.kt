package com.cheesecomer.rewardseal.data.source.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "completed_reward_sheets")
data class CompletedRewardSheetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sheetId: Long,
    val title: String,
    val goalCount: Int,
    val completedAt: LocalDateTime,
    val consumedAt: LocalDateTime?,
)