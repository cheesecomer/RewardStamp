package com.cheesecomer.rewardseal.data.source.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reward_milestones")
data class RewardMilestoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sheetId: Long,
    val requiredCompletions: Int,
    val reward: String,
)
