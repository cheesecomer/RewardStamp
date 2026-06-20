package com.cheesecomer.rewardseal

import android.app.Application
import androidx.room.Room
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardseal.data.repository.ExchangeableSheetRepository
import com.cheesecomer.rewardseal.data.repository.RewardMilestoneRepository
import com.cheesecomer.rewardseal.data.repository.RewardSheetRepository
import com.cheesecomer.rewardseal.data.repository.RewardStampRepository
import com.cheesecomer.rewardseal.data.source.database.AppDatabase

@ExcludeFromCoverage
class RewardSealApplication : Application() {
    val database: AppDatabase by lazy {
        Room
            .databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "reward_seal.db",
            ).build()
    }

    val rewardSheetRepository: RewardSheetRepository by lazy {
        RewardSheetRepository(database.rewardSheetDao())
    }

    val completedRewardSheetRepository: CompletedRewardSheetRepository by lazy {
        CompletedRewardSheetRepository(database.completedRewardSheetDao())
    }
    val rewardStampRepository: RewardStampRepository by lazy {
        RewardStampRepository(database.rewardStampDao())
    }

    val rewardMilestoneRepository: RewardMilestoneRepository by lazy {
        RewardMilestoneRepository(database.rewardMilestoneDao())
    }

    val exchangeableSheetRepository: ExchangeableSheetRepository by lazy {
        ExchangeableSheetRepository(
            rewardSheetRepository,
            completedRewardSheetRepository,
            rewardMilestoneRepository,
        )
    }
}
