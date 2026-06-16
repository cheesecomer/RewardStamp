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
    ORDER BY requiredCompletions ASC
    """,
    )
    suspend fun findBySheetId(sheetId: Long): List<RewardMilestoneEntity>

    @Query(
        """
    SELECT *
    FROM reward_milestones
    WHERE sheetId = :sheetId
      AND :requiredCompletions >= requiredCompletions
    ORDER BY requiredCompletions ASC
    """,
    )
    suspend fun findExchangeableBySheetId(
        sheetId: Long,
        requiredCompletions: Int,
    ): List<RewardMilestoneEntity>

    @Query(
        """
    SELECT *
    FROM reward_milestones
    WHERE sheetId = :sheetId
      AND :requiredCompletions < requiredCompletions
    ORDER BY requiredCompletions ASC
    LIMIT 1
    """,
    )
    suspend fun findNext(
        sheetId: Long,
        requiredCompletions: Int,
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
