package com.cheesecomer.rewardseal.data.source.database.mapper

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cheesecomer.rewardseal.data.source.database.entity.CompletedRewardSheetEntity
import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import java.time.LocalDateTime

fun CompletedRewardSheetEntity.toModel(): CompletedRewardSheet {
    return CompletedRewardSheet(
        id = id,
        sheetId = sheetId,
        title = title,
        goalCount = goalCount,
        completedAt = completedAt,
        consumedAt = consumedAt
    )
}
fun CompletedRewardSheet.toEntity(): CompletedRewardSheetEntity {
    return CompletedRewardSheetEntity(
        id = id,
        sheetId = sheetId,
        title = title,
        goalCount = goalCount,
        completedAt = completedAt,
        consumedAt = consumedAt
    )
}