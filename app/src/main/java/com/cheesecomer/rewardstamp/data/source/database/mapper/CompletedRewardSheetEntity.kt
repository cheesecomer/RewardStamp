package com.cheesecomer.rewardstamp.data.source.database.mapper

import com.cheesecomer.rewardstamp.data.source.database.entity.CompletedRewardSheetEntity
import com.cheesecomer.rewardstamp.model.CompletedRewardSheet
import com.cheesecomer.rewardstamp.model.GoalStampType

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
