package com.cheesecomer.rewardseal.data.source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cheesecomer.rewardseal.data.source.database.entity.RewardStampEntity

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

    @Insert
    suspend fun insert(entity: RewardStampEntity): Long

    @Query("DELETE FROM reward_stamps WHERE sheetId = :sheetId")
    suspend fun deleteBySheetId(sheetId: Long)

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
}
