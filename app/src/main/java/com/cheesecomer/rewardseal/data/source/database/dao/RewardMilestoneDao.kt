package com.cheesecomer.rewardseal.data.source.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cheesecomer.rewardseal.data.source.database.entity.RewardMilestoneEntity

@Dao
interface RewardMilestoneDao {
    @Query(
        """
    SELECT *
    FROM reward_milestones
    WHERE sheetId = :sheetId
    ORDER BY requiredSheetCount ASC
    """,
    )
    suspend fun findBySheetId(sheetId: Long): List<RewardMilestoneEntity>

    @Query(
        """
    SELECT *
    FROM reward_milestones
    WHERE sheetId = :sheetId
      AND :exchangeableSheetCount >= requiredSheetCount
    ORDER BY requiredSheetCount ASC
    """,
    )
    suspend fun findExchangeableMilestonesBySheetId(
        sheetId: Long,
        exchangeableSheetCount: Int,
    ): List<RewardMilestoneEntity>

    @Query(
        """
    SELECT *
    FROM reward_milestones
    WHERE sheetId = :sheetId
      AND :exchangeableSheetCount < requiredSheetCount
    ORDER BY requiredSheetCount ASC
    """,
    )
    suspend fun findLockedMilestonesBySheetId(
        sheetId: Long,
        exchangeableSheetCount: Int,
    ): List<RewardMilestoneEntity>

    @Query(
        """
    SELECT *
    FROM reward_milestones
    WHERE sheetId = :sheetId
      AND :exchangeableSheetCount < requiredSheetCount
    ORDER BY requiredSheetCount ASC
    LIMIT 1
    """,
    )
    suspend fun findClosest(
        sheetId: Long,
        exchangeableSheetCount: Int,
    ): RewardMilestoneEntity?

    @Insert
    suspend fun insert(entity: RewardMilestoneEntity): Long

    @Update
    suspend fun update(entity: RewardMilestoneEntity)

    @Query(
        """
    DELETE FROM reward_milestones
    WHERE sheetId = :sheetId
      AND id NOT IN (:ids)
""",
    )
    suspend fun deleteNotIn(
        sheetId: Long,
        ids: List<Long>,
    )

    @Delete
    suspend fun delete(entity: RewardMilestoneEntity)
}
