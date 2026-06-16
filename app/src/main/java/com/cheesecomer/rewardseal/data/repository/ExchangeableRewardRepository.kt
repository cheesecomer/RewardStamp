package com.cheesecomer.rewardseal.data.repository

import com.cheesecomer.rewardseal.model.ExchangeableSheet

class ExchangeableRewardRepository(
    private val rewardSheetRepository: RewardSheetRepository,
    private val completedRewardSheetRepository: CompletedRewardSheetRepository,
    private val rewardMilestoneRepository: RewardMilestoneRepository,
) {
    suspend fun findAll(): List<ExchangeableSheet> =
        rewardSheetRepository
            .findExchangeable()
            .map { sheet ->
                val completedCount =
                    completedRewardSheetRepository.countExchangeableBySheetId(sheet.id)

                ExchangeableSheet(
                    id = sheet.id,
                    title = sheet.title,
                    unconsumedCompletedCount = completedCount,
                    exchangeableMilestones =
                        rewardMilestoneRepository.findExchangeableBySheetId(
                            sheetId = sheet.id,
                            completedCount = completedCount,
                        ),
                    nextMilestone =
                        rewardMilestoneRepository.findNext(
                            sheetId = sheet.id,
                            completedCount = completedCount,
                        ),
                )
            }

    suspend fun exchangeReward(
        sheetId: Long,
        take: Int,
    ) = completedRewardSheetRepository.markRewardReceived(sheetId, take)
}
