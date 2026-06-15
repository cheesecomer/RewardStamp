package com.cheesecomer.rewardseal.data.source.database.mapper

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cheesecomer.rewardseal.data.source.database.entity.RewardSheetEntity
import com.cheesecomer.rewardseal.model.RewardSheet

fun RewardSheetEntity.toModel(): RewardSheet {
    return RewardSheet(
        id = id,
        title = title,
        currentCount = currentCount,
        goalCount = goalCount,
    )
}
fun RewardSheet.toEntity(): RewardSheetEntity {
    return RewardSheetEntity(
        id = id,
        title = title,
        currentCount = currentCount,
        goalCount = goalCount,
        deletedAt = null
    )
}