package com.cheesecomer.rewardstamp.data.source.database.mapper

import com.cheesecomer.rewardstamp.data.source.database.entity.RewardMilestoneEntity
import com.cheesecomer.rewardstamp.model.RewardMilestone

fun RewardMilestoneEntity.toModel(): RewardMilestone =
    RewardMilestone(
        id = id,
        sheetId = sheetId,
        requiredSheetCount = requiredSheetCount,
        reward = reward,
    )

fun RewardMilestone.toEntity(): RewardMilestoneEntity =
    RewardMilestoneEntity(
        id = id,
        sheetId = sheetId,
        requiredSheetCount = requiredSheetCount,
        reward = reward,
    )
