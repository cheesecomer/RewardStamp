package com.cheesecomer.rewardseal.data.source.database.mapper

import com.cheesecomer.rewardseal.data.source.database.entity.CompletedRewardSheetEntity
import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import com.cheesecomer.rewardseal.model.GoalStampType

fun CompletedRewardSheetEntity.toModel(): CompletedRewardSheet =
    CompletedRewardSheet(
        id = id,
        sheetId = sheetId,
        title = title,
        goalCount = goalCount,
        goalStampType = GoalStampType.fromId(stampTypeId),
        completedAt = completedAt,
        consumedAt = consumedAt,
    )

fun CompletedRewardSheet.toEntity(): CompletedRewardSheetEntity =
    CompletedRewardSheetEntity(
        id = id,
        sheetId = sheetId,
        title = title,
        goalCount = goalCount,
        stampTypeId = goalStampType.id,
        completedAt = completedAt,
        consumedAt = consumedAt,
    )
