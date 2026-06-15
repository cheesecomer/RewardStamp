package com.cheesecomer.rewardseal.data.repository

import com.cheesecomer.rewardseal.data.source.database.dao.CompletedRewardSheetDao
import com.cheesecomer.rewardseal.data.source.database.mapper.toEntity
import com.cheesecomer.rewardseal.data.source.database.mapper.toModel
import com.cheesecomer.rewardseal.model.CompletedRewardSheet

class CompletedRewardSheetRepository(
    private val dao: CompletedRewardSheetDao
) {
    suspend fun save(completedSheet: CompletedRewardSheet): Long {
        return dao.insert(completedSheet.toEntity())
    }

    suspend fun findAll(): List<CompletedRewardSheet> {
        return dao.findAll()
            .map { it.toModel() }
    }

    suspend fun findUnreceived(): List<CompletedRewardSheet> {
        return dao.findUnreceived()
            .map { it.toModel() }
    }
    suspend fun findById(id: Long): CompletedRewardSheet? {
        return dao.findById(id)
            ?.toModel()
    }
    suspend fun countAll(): Int {
        return dao.countAll()
    }

    suspend fun countUnreceived(): Int {
        return dao.countUnreceived()
    }

    suspend fun markRewardReceived(id: Long) {
        val sheet = dao.findById(id)
        if (sheet == null) {
            return
        }

        dao.update(sheet.copy(rewardReceived = true))
    }
}