package com.cheesecomer.rewardseal.data.source.database

import com.cheesecomer.rewardseal.data.source.database.entity.CompletedRewardSheetEntity
import com.cheesecomer.rewardseal.data.source.database.entity.RewardMilestoneEntity
import com.cheesecomer.rewardseal.data.source.database.entity.RewardSheetEntity
import com.cheesecomer.rewardseal.data.source.database.entity.RewardStampEntity
import com.cheesecomer.rewardseal.model.StampType
import java.time.LocalDateTime

fun rewardSheetEntity(
    id: Long = 0L,
    title: String = "はみがき",
    currentCount: Int = 0,
    goalCount: Int = 10,
    deletedAt: LocalDateTime? = null,
): RewardSheetEntity =
    RewardSheetEntity(
        id = id,
        title = title,
        currentCount = currentCount,
        goalCount = goalCount,
        deletedAt = deletedAt,
    )

fun completedRewardSheetEntity(
    id: Long = 0L,
    sheetId: Long = 1,
    title: String = "はみがき",
    goalCount: Int = 10,
    completedAt: LocalDateTime = LocalDateTime.now(),
    consumedAt: LocalDateTime? = null,
): CompletedRewardSheetEntity =
    CompletedRewardSheetEntity(
        id = id,
        sheetId = sheetId,
        title = title,
        goalCount = goalCount,
        completedAt = completedAt,
        consumedAt = consumedAt,
    )

fun rewardMilestoneEntity(
    id: Long = 0L,
    sheetId: Long = 1,
    requiredSheetCount: Int = 1,
    reward: String = "アイス",
): RewardMilestoneEntity =
    RewardMilestoneEntity(
        id = id,
        sheetId = sheetId,
        requiredSheetCount = requiredSheetCount,
        reward = reward,
    )

fun rewardStampEntity(
    id: Long = 0L,
    sheetId: Long = 1,
    completedRewardSheetId: Long? = null,
    position: Int = 0,
    stampType: StampType = StampType.Hippopotamus,
    stampedAt: LocalDateTime = LocalDateTime.now(),
): RewardStampEntity =
    RewardStampEntity(
        id = id,
        sheetId = sheetId,
        completedRewardSheetId = completedRewardSheetId,
        position = position,
        stampTypeId = stampType.id,
        stampedAt = stampedAt,
    )
