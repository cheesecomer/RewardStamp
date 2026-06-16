package com.cheesecomer.rewardseal.data.source.database.mapper

import com.cheesecomer.rewardseal.data.source.database.entity.CompletedRewardSheetEntity
import com.cheesecomer.rewardseal.model.CompletedRewardSheet

fun CompletedRewardSheetEntity.toModel(): CompletedRewardSheet =
    CompletedRewardSheet(
        id = id,
        sheetId = sheetId,
        title = title,
        goalCount = goalCount,
        completedAt = completedAt,
        consumedAt = consumedAt,
    )

fun CompletedRewardSheet.toEntity(): CompletedRewardSheetEntity =
    CompletedRewardSheetEntity(
        id = id,
        sheetId = sheetId,
        title = title,
        goalCount = goalCount,
        completedAt = completedAt,
        consumedAt = consumedAt,
    )
