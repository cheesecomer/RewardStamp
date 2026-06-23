package com.cheesecomer.rewardstamp.data.repository

import com.cheesecomer.rewardstamp.data.source.database.dao.RewardStampDao
import com.cheesecomer.rewardstamp.data.source.database.mapper.toEntity
import com.cheesecomer.rewardstamp.data.source.database.mapper.toModel
import com.cheesecomer.rewardstamp.model.RewardStamp

class RewardStampRepository(
    private val dao: RewardStampDao,
) {
    suspend fun findBySheetId(sheetId: Long): List<RewardStamp> = dao.findBySheetId(sheetId).map { it.toModel() }

    suspend fun findByCompletedRewardSheetId(completedRewardSheetId: Long): List<RewardStamp> =
        dao.findByCompletedRewardSheetId(completedRewardSheetId).map {
            it.toModel()
        }

    suspend fun save(stamp: RewardStamp) {
        dao.insert(stamp.toEntity())
    }

    suspend fun attachToCompletedRewardSheet(
        sheetId: Long,
        completedRewardSheetId: Long,
    ) {
        dao.attachToCompletedRewardSheet(sheetId, completedRewardSheetId)
    }

    suspend fun findLatestByEachSheet() = dao.findLatestByEachSheet().associate { it.sheetId to it.toModel() }
}
