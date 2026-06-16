package com.cheesecomer.rewardseal.data.repository

import com.cheesecomer.rewardseal.data.source.database.dao.CompletedRewardSheetDao
import com.cheesecomer.rewardseal.data.source.database.mapper.toEntity
import com.cheesecomer.rewardseal.data.source.database.mapper.toModel
import com.cheesecomer.rewardseal.model.CompletedRewardSheet
import java.time.LocalDateTime

class CompletedRewardSheetRepository(
    private val dao: CompletedRewardSheetDao,
) {
    suspend fun save(completedSheet: CompletedRewardSheet): Long = dao.insert(completedSheet.toEntity())

    suspend fun findAll(): List<CompletedRewardSheet> =
        dao
            .findAll()
            .map { it.toModel() }

    suspend fun findUnreceived(): List<CompletedRewardSheet> =
        dao
            .findUnreceived()
            .map { it.toModel() }

    suspend fun findById(id: Long): CompletedRewardSheet? =
        dao
            .findById(id)
            ?.toModel()

    suspend fun countAll(): Int = dao.countAll()

    suspend fun countUnreceived(): Int = dao.countUnreceived()

    suspend fun countExchangeableBySheetId(sheetId: Long): Int = dao.countExchangeableBySheetId(sheetId)

    suspend fun markRewardReceived(
        sheetId: Long,
        take: Int,
    ) {
        val sheets = dao.findUnreceivedBySheetId(sheetId, take)
        sheets.forEach { sheet ->
            dao.update(sheet.copy(consumedAt = LocalDateTime.now()))
        }
    }
}
