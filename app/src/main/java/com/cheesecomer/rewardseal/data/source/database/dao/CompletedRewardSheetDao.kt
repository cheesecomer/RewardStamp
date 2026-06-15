package com.cheesecomer.rewardseal.data.source.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cheesecomer.rewardseal.data.source.database.entity.CompletedRewardSheetEntity

@Dao
interface CompletedRewardSheetDao {
    @Query("SELECT * FROM completed_reward_sheets ORDER BY completedAt DESC")
    suspend fun findAll(): List<CompletedRewardSheetEntity>

    @Query("SELECT * FROM completed_reward_sheets WHERE sheetId = :sheetId AND consumedAt IS NULL ORDER BY completedAt ASC LIMIT :take")
    suspend fun findUnreceivedBySheetId(sheetId: Long, take: Int): List<CompletedRewardSheetEntity>

    @Query("SELECT * FROM completed_reward_sheets WHERE id = :id")
    suspend fun findById(id: Long): CompletedRewardSheetEntity?

    @Query("SELECT * FROM completed_reward_sheets WHERE consumedAt IS NULL")
    suspend fun findUnreceived(): List<CompletedRewardSheetEntity>

    @Query("SELECT COUNT(*) FROM completed_reward_sheets")
    suspend fun countAll(): Int

    @Query("SELECT COUNT(*) FROM completed_reward_sheets WHERE consumedAt IS NULL")
    suspend fun countUnreceived(): Int

    @Query("SELECT COUNT(*) FROM completed_reward_sheets WHERE sheetId = :sheetId AND consumedAt IS NULL")

    suspend fun countExchangeableBySheetId(sheetId: Long): Int

    @Insert
    suspend fun insert(entity: CompletedRewardSheetEntity): Long

    @Update
    suspend fun update(entity: CompletedRewardSheetEntity)
}