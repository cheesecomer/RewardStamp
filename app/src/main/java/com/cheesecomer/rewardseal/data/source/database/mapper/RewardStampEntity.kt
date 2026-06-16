package com.cheesecomer.rewardseal.data.source.database.mapper

import com.cheesecomer.rewardseal.data.source.database.entity.RewardStampEntity
import com.cheesecomer.rewardseal.model.RewardStamp
import com.cheesecomer.rewardseal.model.StampType

fun RewardStampEntity.toModel(): RewardStamp =
    RewardStamp(
        id = id,
        sheetId = sheetId,
        completedRewardSheetId = completedRewardSheetId,
        position = position,
        stampType = StampType.fromId(stampTypeId),
        stampedAt = stampedAt,
    )

fun RewardStamp.toEntity(): RewardStampEntity =
    RewardStampEntity(
        id = id,
        sheetId = sheetId,
        completedRewardSheetId = completedRewardSheetId,
        position = position,
        stampTypeId = stampType.id,
        stampedAt = stampedAt,
    )
