package com.cheesecomer.rewardstamp.data.repository

import com.cheesecomer.rewardstamp.model.ExchangeableSheet

class ExchangeableSheetRepository(
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
    ) = completedRewardSheetRepository.markRewardReceived(sheetId, exchangeCount)
}
