package com.cheesecomer.rewardstamp.data.repository

import com.cheesecomer.rewardstamp.data.source.database.TransactionRunner
import com.cheesecomer.rewardstamp.model.ExchangeableSheet

class ExchangeableSheetRepository(
    private val transactionRunner: TransactionRunner,
    private val rewardSheetRepository: RewardSheetRepository,
    private val completedRewardSheetRepository: CompletedRewardSheetRepository,
    private val rewardMilestoneRepository: RewardMilestoneRepository,
) {
    suspend fun findAll(): List<ExchangeableSheet> =
        rewardSheetRepository
            .findExchangeable()
            .map { sheet ->
                val exchangeableSheetCount =
                    completedRewardSheetRepository.countExchangeableBySheetId(sheet.id)

                ExchangeableSheet(
                    rewardSheet = sheet,
                    exchangeableSheetCount = exchangeableSheetCount,
                    exchangeableMilestones =
                        rewardMilestoneRepository.findExchangeableMilestonesBySheetId(
                            sheetId = sheet.id,
                            exchangeableSheetCount = exchangeableSheetCount,
                        ),
                    closestMilestone =
                        rewardMilestoneRepository.findClosest(
                            sheetId = sheet.id,
                            exchangeableSheetCount = exchangeableSheetCount,
                        ),
                )
            }

    suspend fun exchangeReward(
        sheetId: Long,
        exchangeCount: Int,
    ): Boolean =
        transactionRunner.run {
            val exchangeableSheetCount =
                completedRewardSheetRepository.countExchangeableBySheetId(sheetId)
            if (exchangeCount < 1 || exchangeableSheetCount < exchangeCount) {
                return@run false
            }

            completedRewardSheetRepository.markRewardReceived(sheetId, exchangeCount)
            true
        }
}
