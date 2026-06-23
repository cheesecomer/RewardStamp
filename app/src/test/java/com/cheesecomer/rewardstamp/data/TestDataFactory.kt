package com.cheesecomer.rewardstamp.data

import com.cheesecomer.rewardstamp.model.CompletedRewardSheet
import com.cheesecomer.rewardstamp.model.ExchangeableSheet
import com.cheesecomer.rewardstamp.model.GoalStampType
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.model.RewardStamp
import com.cheesecomer.rewardstamp.model.StampType
import java.time.LocalDateTime

fun rewardSheet(
    id: Long = 0L,
    title: String = "はみがき",
    currentCount: Int = 0,
    goalCount: Int = 10,
): RewardSheet =
    RewardSheet(
        id = id,
        title = title,
        currentCount = currentCount,
        goalCount = goalCount,
    )

fun completedRewardSheet(
    id: Long = 0L,
    sheetId: Long = 1,
    title: String = "はみがき",
    goalCount: Int = 10,
    goalStampType: GoalStampType = GoalStampType.Bear1,
    completedAt: LocalDateTime = LocalDateTime.now(),
    consumedAt: LocalDateTime? = null,
): CompletedRewardSheet =
    CompletedRewardSheet(
        id = id,
        sheetId = sheetId,
        title = title,
        goalCount = goalCount,
        goalStampType = goalStampType,
        completedAt = completedAt,
        consumedAt = consumedAt,
    )

fun rewardMilestone(
    id: Long = 0L,
    sheetId: Long = 1,
    requiredSheetCount: Int = 1,
    reward: String = "アイス",
): RewardMilestone =
    RewardMilestone(
        id = id,
        sheetId = sheetId,
        requiredSheetCount = requiredSheetCount,
        reward = reward,
    )

fun rewardStamp(
    id: Long = 1,
    sheetId: Long = 1,
    completedRewardSheetId: Long? = null,
    position: Int = 0,
    stampType: StampType = StampType.Hippopotamus,
    stampedAt: LocalDateTime = LocalDateTime.now(),
): RewardStamp =
    RewardStamp(
        id = id,
        sheetId = sheetId,
        completedRewardSheetId = completedRewardSheetId,
        position = position,
        stampType = stampType,
        stampedAt = stampedAt,
    )

fun exchangeableSheet(
    id: Long = 1,
    title: String = "Sheet",
    unconsumedCompletedCount: Int = 10,
    exchangeableMilestones: List<RewardMilestone> =
        listOf(
            rewardMilestone(
                requiredSheetCount = 1,
                reward = "アイス",
            ),
        ),
    nextMilestone: RewardMilestone? = null,
): ExchangeableSheet =
    ExchangeableSheet(
        rewardSheet =
            rewardSheet(
                id = id,
                title = title,
            ),
        exchangeableSheetCount = unconsumedCompletedCount,
        exchangeableMilestones = exchangeableMilestones,
        closestMilestone = nextMilestone,
    )
