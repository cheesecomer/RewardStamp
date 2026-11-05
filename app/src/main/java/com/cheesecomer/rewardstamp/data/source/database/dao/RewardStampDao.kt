package com.cheesecomer.rewardstamp.data.source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.cheesecomer.rewardstamp.data.source.database.entity.RewardStampEntity

@Dao
interface RewardStampDao {
    @Query(
        """
    SELECT *
    FROM   reward_stamps
    WHERE sheetId = :sheetId
      AND completedRewardSheetId IS NULL
    ORDER BY position ASC
    """,
    )
    suspend fun findBySheetId(sheetId: Long): List<RewardStampEntity>

    @Query(
        """
    SELECT *
    FROM   reward_stamps
    WHERE  completedRewardSheetId = :completedRewardSheetId
    ORDER BY position ASC
    """,
    )
    suspend fun findByCompletedRewardSheetId(completedRewardSheetId: Long): List<RewardStampEntity>

    @Query(
        """
    SELECT *
    FROM reward_stamps
    WHERE id IN (
        SELECT id
        FROM reward_stamps AS rs
        WHERE rs.sheetId = reward_stamps.sheetId
        ORDER BY
            rs.stampedAt DESC,
            rs.position DESC,
            rs.id DESC
        LIMIT 1
    )
      AND sheetId IN (SELECT id FROM reward_sheets WHERE deletedAt IS NULL)
      AND completedRewardSheetId IS NULL
    """,
    )
    suspend fun findLatestByEachSheet(): List<RewardStampEntity>

    @Insert
    suspend fun insert(entity: RewardStampEntity): Long

    @Query(
        """
    UPDATE reward_stamps
    SET completedRewardSheetId = :completedRewardSheetId
    WHERE sheetId = :sheetId
      AND completedRewardSheetId IS NULL
    """,
    )
    suspend fun attachToCompletedRewardSheet(
        sheetId: Long,
        completedRewardSheetId: Long,
    )

    @Query("UPDATE reward_stamps SET stampTypeId = :stampTypeId WHERE id = :id")
    suspend fun updateStampType(
        id: Long,
        stampTypeId: String,
    )

    @Query("DELETE FROM reward_stamps WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query(
        """
    UPDATE reward_stamps
    SET position = position - 1
    WHERE sheetId = :sheetId
      AND completedRewardSheetId IS NULL
      AND position > :position
    """,
    )
    suspend fun shiftPositionsAfter(
        sheetId: Long,
        position: Int,
    )

    @Query("UPDATE reward_sheets SET currentCount = currentCount - 1 WHERE id = :sheetId AND currentCount > 0")
    suspend fun decrementSheetCount(sheetId: Long)

    // Removes the stamp and moves the later stamps forward so the positions stay contiguous
    @Transaction
    suspend fun cancel(entity: RewardStampEntity) {
        deleteById(entity.id)
        shiftPositionsAfter(entity.sheetId, entity.position)
        decrementSheetCount(entity.sheetId)
    }
}
