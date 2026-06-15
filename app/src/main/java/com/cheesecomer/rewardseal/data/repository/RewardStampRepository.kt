package com.cheesecomer.rewardseal.data.repository

import com.cheesecomer.rewardseal.data.source.database.dao.RewardStampDao
import com.cheesecomer.rewardseal.data.source.database.mapper.toEntity
import com.cheesecomer.rewardseal.data.source.database.mapper.toModel
import com.cheesecomer.rewardseal.model.RewardStamp

class RewardStampRepository(
    private val dao: RewardStampDao,
) {
    suspend fun findBySheetId(sheetId: Long): List<RewardStamp> {
        return dao.findBySheetId(sheetId).map { it.toModel() }
    }
    suspend fun findByCompletedRewardSheetId(completedRewardSheetId: Long): List<RewardStamp> {
        return dao.findByCompletedRewardSheetId(completedRewardSheetId).map { it.toModel() }
    }

    suspend fun save(stamp: RewardStamp) {
        dao.insert(stamp.toEntity())
    }

    suspend fun deleteBySheetId(sheetId: Long) {
        dao.deleteBySheetId(sheetId)
    }

    suspend fun attachToCompletedRewardSheet(
        sheetId: Long,
        completedRewardSheetId: Long
    ) {
        dao.attachToCompletedRewardSheet(sheetId, completedRewardSheetId)
    }

}