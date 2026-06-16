package com.cheesecomer.rewardseal.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cheesecomer.rewardseal.data.source.database.dao.CompletedRewardSheetDao
import com.cheesecomer.rewardseal.data.source.database.dao.RewardMilestoneDao
import com.cheesecomer.rewardseal.data.source.database.dao.RewardSheetDao
import com.cheesecomer.rewardseal.data.source.database.dao.RewardStampDao
import com.cheesecomer.rewardseal.data.source.database.entity.CompletedRewardSheetEntity
import com.cheesecomer.rewardseal.data.source.database.entity.RewardMilestoneEntity
import com.cheesecomer.rewardseal.data.source.database.entity.RewardSheetEntity
import com.cheesecomer.rewardseal.data.source.database.entity.RewardStampEntity

@Database(
    exportSchema = true,
    entities = [
        RewardSheetEntity::class,
        CompletedRewardSheetEntity::class,
        RewardStampEntity::class,
        RewardMilestoneEntity::class,
    ],
    version = 1,
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rewardSheetDao(): RewardSheetDao

    abstract fun completedRewardSheetDao(): CompletedRewardSheetDao

    abstract fun rewardStampDao(): RewardStampDao

    abstract fun rewardMilestoneDao(): RewardMilestoneDao
}
