package com.cheesecomer.rewardseal.data.source.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cheesecomer.rewardseal.data.source.database.entity.RewardSheetEntity

@Dao
interface RewardSheetDao {
    @Query("SELECT * FROM reward_sheets WHERE deletedAt IS NULL")
    suspend fun findAll(): List<RewardSheetEntity>

    @Query(
        """
        SELECT *
        FROM reward_sheets
        WHERE id IN (
            SELECT reward_milestones.sheetId
            FROM reward_milestones
            INNER JOIN (
                SELECT sheetId, count(*) as completionCount
                FROM completed_reward_sheets
                WHERE consumedAt IS NULL
                GROUP BY sheetId
            )  as completed_reward_sheets
                ON  completed_reward_sheets.sheetId = reward_milestones.sheetId 
                AND reward_milestones.requiredSheetCount <= completed_reward_sheets.completionCount
        )
    """,
    )
    suspend fun findExchangeable(): List<RewardSheetEntity>

    @Query(
        """
    SELECT count(*)
    FROM reward_sheets
    WHERE id IN (
        SELECT sheetId FROM completed_reward_sheets WHERE consumedAt IS NULL
    )
    """,
    )
    suspend fun countExchangeable(): Int

    @Query("SELECT * FROM reward_sheets WHERE id = :id")
    suspend fun findById(id: Long): RewardSheetEntity?

    @Insert
    suspend fun insert(entity: RewardSheetEntity): Long

    @Update
    suspend fun update(entity: RewardSheetEntity)

    @Delete
    suspend fun delete(entity: RewardSheetEntity)
}
