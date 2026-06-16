package com.cheesecomer.rewardseal.data.repository

import com.cheesecomer.rewardseal.data.source.database.dao.RewardMilestoneDao
import com.cheesecomer.rewardseal.data.source.database.mapper.toEntity
import com.cheesecomer.rewardseal.data.source.database.mapper.toModel
import com.cheesecomer.rewardseal.model.RewardMilestone

class RewardMilestoneRepository(
    private val dao: RewardMilestoneDao,
) {
    suspend fun findBySheetId(sheetId: Long): List<RewardMilestone> = dao.findBySheetId(sheetId).map { it.toModel() }

    suspend fun findExchangeableBySheetId(
        sheetId: Long,
        completedCount: Int,
    ): List<RewardMilestone> =
        dao.findExchangeableBySheetId(sheetId, completedCount).map {
            it.toModel()
        }

    suspend fun findNext(
        sheetId: Long,
        completedCount: Int,
    ): RewardMilestone? = dao.findNext(sheetId, completedCount)?.toModel()

    suspend fun saveAll(
        sheetId: Long,
        rewardMilestones: List<RewardMilestone>,
    ) {
        val ids = rewardMilestones.map { it.id }.filter { it != 0L }
        if (ids.isNotEmpty()) {
            dao.deleteNotIn(sheetId, ids)
        }

        rewardMilestones.forEach {
            val entity = it.toEntity()
            if (it.id == 0L) {
                dao.insert(entity)
            } else {
                dao.update(entity)
            }
        }
    }
}
