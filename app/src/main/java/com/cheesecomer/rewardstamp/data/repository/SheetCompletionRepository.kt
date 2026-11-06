package com.cheesecomer.rewardstamp.data.repository

import com.cheesecomer.rewardstamp.data.source.database.TransactionRunner
import com.cheesecomer.rewardstamp.model.CompletedRewardSheet
import com.cheesecomer.rewardstamp.model.GoalStampType
import java.time.LocalDateTime

class SheetCompletionRepository(
    private val transactionRunner: TransactionRunner,
    private val rewardSheetRepository: RewardSheetRepository,
    private val completedRewardSheetRepository: CompletedRewardSheetRepository,
    private val rewardStampRepository: RewardStampRepository,
    private val now: () -> LocalDateTime = { LocalDateTime.now() },
    private val pickGoalStampType: () -> GoalStampType = { GoalStampType.entries.random() },
) {
    // Returns the goal stamp put on the completed sheet, or null when the sheet is not completed
    suspend fun complete(sheetId: Long): GoalStampType? =
        transactionRunner.run {
            val sheet = rewardSheetRepository.findById(sheetId)
            if (sheet == null || !sheet.isCompleted) {
                return@run null
            }

            val goalStampType = pickGoalStampType()
            val completedRewardSheetId =
                completedRewardSheetRepository.save(
                    CompletedRewardSheet(
                        id = 0,
                        sheetId = sheet.id,
                        title = sheet.title,
                        goalCount = sheet.goalCount,
                        goalStampType = goalStampType,
                        completedAt = now(),
                        consumedAt = null,
                    ),
                )
            rewardStampRepository.attachToCompletedRewardSheet(
                sheetId = sheet.id,
                completedRewardSheetId = completedRewardSheetId,
            )
            rewardSheetRepository.restart(sheet.id)

            goalStampType
        }
}
