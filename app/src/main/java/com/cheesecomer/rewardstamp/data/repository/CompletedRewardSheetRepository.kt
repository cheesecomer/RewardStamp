package com.cheesecomer.rewardstamp.data.repository

import com.cheesecomer.rewardstamp.data.source.database.dao.CompletedRewardSheetDao
import com.cheesecomer.rewardstamp.data.source.database.mapper.toEntity
import com.cheesecomer.rewardstamp.data.source.database.mapper.toModel
import com.cheesecomer.rewardstamp.model.CompletedRewardSheet
import java.time.LocalDateTime

class CompletedRewardSheetRepository(
    private val dao: CompletedRewardSheetDao,
    private val now: () -> LocalDateTime = { LocalDateTime.now() },
) {
    suspend fun save(completedSheet: CompletedRewardSheet): Long = dao.insert(completedSheet.toEntity())

    suspend fun findAll(): List<CompletedRewardSheet> =
        dao
            .findAll()
            .map { it.toModel() }

    suspend fun findById(id: Long): CompletedRewardSheet? =
        dao
            .findById(id)
            ?.toModel()

    suspend fun countAll(): Int = dao.countAll()

    suspend fun countExchangeableBySheetId(sheetId: Long): Int = dao.countExchangeableBySheetId(sheetId)

    suspend fun markRewardReceived(
        sheetId: Long,
        exchangeCount: Int,
    ) {
        val consumedAt = now()

        dao.findUnreceivedBySheetId(sheetId, exchangeCount).forEach { sheet ->
            dao.update(
                sheet.copy(
                    consumedAt = consumedAt,
                ),
            )
        }
    }
}
