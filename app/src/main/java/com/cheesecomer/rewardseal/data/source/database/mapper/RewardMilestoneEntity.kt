package com.cheesecomer.rewardseal.data.source.database.mapper

import com.cheesecomer.rewardseal.data.source.database.entity.RewardMilestoneEntity
import com.cheesecomer.rewardseal.model.RewardMilestone

fun RewardMilestoneEntity.toModel(): RewardMilestone =
    RewardMilestone(
        id = id,
        sheetId = sheetId,
        requiredCompletions = requiredCompletions,
        reward = reward,
    )

fun RewardMilestone.toEntity(): RewardMilestoneEntity =
    RewardMilestoneEntity(
        id = id,
        sheetId = sheetId,
        requiredCompletions = requiredCompletions,
        reward = reward,
    )
