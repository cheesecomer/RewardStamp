package com.cheesecomer.rewardstamp.data.source.database.mapper

import com.cheesecomer.rewardstamp.data.source.database.entity.RewardSheetEntity
import com.cheesecomer.rewardstamp.model.RewardSheet

fun RewardSheetEntity.toModel(): RewardSheet =
    RewardSheet(
        id = id,
        title = title,
        currentCount = currentCount,
        goalCount = goalCount,
    )

fun RewardSheet.toEntity(): RewardSheetEntity =
    RewardSheetEntity(
        id = id,
        title = title,
        currentCount = currentCount,
        goalCount = goalCount,
        deletedAt = null,
    )
