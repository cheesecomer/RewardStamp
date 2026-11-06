package com.cheesecomer.rewardstamp

import android.app.Application
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardstamp.data.repository.ExchangeableSheetRepository
import com.cheesecomer.rewardstamp.data.repository.RewardMilestoneRepository
import com.cheesecomer.rewardstamp.data.repository.RewardSheetRepository
import com.cheesecomer.rewardstamp.data.repository.RewardStampRepository
import com.cheesecomer.rewardstamp.data.repository.SettingsRepository
import com.cheesecomer.rewardstamp.data.repository.SheetCompletionRepository
import com.cheesecomer.rewardstamp.data.source.database.AppDatabase
import com.cheesecomer.rewardstamp.data.source.database.RoomTransactionRunner
import com.cheesecomer.rewardstamp.model.GoalStampType
import java.time.Clock
import java.time.LocalDateTime
import kotlin.random.Random

@ExcludeFromCoverage
class RewardStampApplication : Application() {
    // The screenshot tour fixes these, so that the same screenshots are taken every time
    var clock: Clock = Clock.systemDefaultZone()
    var random: Random = Random.Default

    // Confetti uses its own random numbers, so the screenshot tour turns it off instead
    var showsConfetti: Boolean = true

    val now: () -> LocalDateTime = { LocalDateTime.now(clock) }

    val database: AppDatabase by lazy {
        Room
            .databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "reward_seal.db",
            ).build()
    }

    private val transactionRunner by lazy {
        RoomTransactionRunner(database)
    }

    val rewardSheetRepository: RewardSheetRepository by lazy {
        RewardSheetRepository(database.rewardSheetDao(), now)
    }

    val completedRewardSheetRepository: CompletedRewardSheetRepository by lazy {
        CompletedRewardSheetRepository(database.completedRewardSheetDao(), now)
    }
    val rewardStampRepository: RewardStampRepository by lazy {
        RewardStampRepository(database.rewardStampDao())
    }

    val rewardMilestoneRepository: RewardMilestoneRepository by lazy {
        RewardMilestoneRepository(database.rewardMilestoneDao())
    }

    val exchangeableSheetRepository: ExchangeableSheetRepository by lazy {
        ExchangeableSheetRepository(
            transactionRunner,
            rewardSheetRepository,
            completedRewardSheetRepository,
            rewardMilestoneRepository,
        )
    }

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(
            PreferenceDataStoreFactory.create {
                applicationContext.preferencesDataStoreFile("settings")
            },
        )
    }

    val sheetCompletionRepository: SheetCompletionRepository by lazy {
        SheetCompletionRepository(
            transactionRunner,
            rewardSheetRepository,
            completedRewardSheetRepository,
            rewardStampRepository,
            now,
            pickGoalStampType = { GoalStampType.entries.random(random) },
        )
    }
}
