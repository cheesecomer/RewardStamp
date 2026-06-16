package com.cheesecomer.rewardseal.data.repository

import com.cheesecomer.rewardseal.data.source.database.dao.RewardSheetDao
import com.cheesecomer.rewardseal.data.source.database.mapper.toEntity
import com.cheesecomer.rewardseal.data.source.database.mapper.toModel
import com.cheesecomer.rewardseal.model.RewardSheet
import java.time.LocalDateTime

class RewardSheetRepository(
    private val dao: RewardSheetDao,
) {
    suspend fun findById(id: Long): RewardSheet? =
        dao
            .findById(id)
            ?.toModel()

    suspend fun save(sheet: RewardSheet): Long {
        val entity = sheet.toEntity()

        if (sheet.id == 0L) {
            return dao.insert(entity)
        } else {
            dao.update(entity)
            return entity.id
        }
    }

    suspend fun findExchangeable(): List<RewardSheet> =
        dao
            .findExchangeable()
            .map { it.toModel() }

    suspend fun countExchangeable() = dao.countExchangeable()

    suspend fun findAll(): List<RewardSheet> =
        dao
            .findAll()
            .map { it.toModel() }

    suspend fun increment(id: Long): Boolean {
        val sheet = dao.findById(id)
        if (sheet == null || sheet.currentCount >= sheet.goalCount) {
            return false
        }

        dao.update(
            sheet.copy(
                currentCount = sheet.currentCount + 1,
            ),
        )

        return true
    }

    suspend fun restart(id: Long) {
        val sheet = dao.findById(id)
        if (sheet == null) {
            return
        }

        dao.update(sheet.copy(currentCount = 0))
    }

    suspend fun delete(id: Long) {
        val sheet = dao.findById(id)
        if (sheet == null) {
            return
        }

        dao.update(sheet.copy(deletedAt = LocalDateTime.now()))
    }
}
