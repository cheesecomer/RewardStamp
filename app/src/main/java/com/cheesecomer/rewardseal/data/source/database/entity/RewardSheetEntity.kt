package com.cheesecomer.rewardseal.data.source.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "reward_sheets")
data class RewardSheetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val currentCount: Int,
    val goalCount: Int,
    val deletedAt: LocalDateTime?
)