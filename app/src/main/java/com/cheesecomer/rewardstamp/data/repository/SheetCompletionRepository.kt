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
) {
    suspend fun complete(
        sheetId: Long,
        goalStampType: GoalStampType,
    ): Boolean =
        transactionRunner.run {
            val sheet = rewardSheetRepository.findById(sheetId)
            if (sheet == null || !sheet.isCompleted) {
                return@run false
            }

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

            true
        }
}
