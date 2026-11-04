package com.cheesecomer.rewardstamp.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardstamp.data.source.database.AppDatabase
import com.cheesecomer.rewardstamp.data.source.database.RoomTransactionRunner
import com.cheesecomer.rewardstamp.data.source.database.rewardSheetEntity
import com.cheesecomer.rewardstamp.data.source.database.rewardStampEntity
import com.cheesecomer.rewardstamp.model.GoalStampType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class SheetCompletionRepositoryTest {
    private lateinit var database: AppDatabase
    private lateinit var rewardSheetRepository: RewardSheetRepository
    private lateinit var completedRewardSheetRepository: CompletedRewardSheetRepository
    private lateinit var rewardStampRepository: RewardStampRepository
    private lateinit var repository: SheetCompletionRepository

    private val now = LocalDateTime.parse("2026-06-16T17:07:48.429")

    @Before
    fun setUp() {
        database =
            Room
                .inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    AppDatabase::class.java,
                ).allowMainThreadQueries()
                .build()

        rewardSheetRepository = RewardSheetRepository(dao = database.rewardSheetDao())
        completedRewardSheetRepository =
            CompletedRewardSheetRepository(
                dao = database.completedRewardSheetDao(),
                now = { now },
            )
        rewardStampRepository = RewardStampRepository(dao = database.rewardStampDao())

        repository =
            SheetCompletionRepository(
                transactionRunner = RoomTransactionRunner(database),
                rewardSheetRepository = rewardSheetRepository,
                completedRewardSheetRepository = completedRewardSheetRepository,
                rewardStampRepository = rewardStampRepository,
                now = { now },
            )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun complete_createsCompletedSheetAndRestartsSheet() =
        runTest {
            val sheetId =
                database.rewardSheetDao().insert(
                    rewardSheetEntity(
                        title = "はみがき",
                        currentCount = 1,
                        goalCount = 1,
                    ),
                )
            database.rewardStampDao().insert(rewardStampEntity(sheetId = sheetId))

            val result = repository.complete(sheetId, GoalStampType.Heart1)

            val completedSheet = completedRewardSheetRepository.findAll().single()
            assertThat(result).isTrue()
            assertThat(completedSheet.title).isEqualTo("はみがき")
            assertThat(completedSheet.goalStampType).isEqualTo(GoalStampType.Heart1)
            assertThat(completedSheet.completedAt).isEqualTo(now)
            assertThat(rewardStampRepository.findByCompletedRewardSheetId(completedSheet.id)).hasSize(1)
            assertThat(rewardStampRepository.findBySheetId(sheetId)).isEmpty()
            assertThat(rewardSheetRepository.findById(sheetId)!!.currentCount).isEqualTo(0)
        }

    @Test
    fun complete_doesNotCreateDuplicateWhenCalledTwice() =
        runTest {
            val sheetId =
                database.rewardSheetDao().insert(
                    rewardSheetEntity(
                        currentCount = 1,
                        goalCount = 1,
                    ),
                )

            repository.complete(sheetId, GoalStampType.Heart1)
            val result = repository.complete(sheetId, GoalStampType.Heart1)

            assertThat(result).isFalse()
            assertThat(completedRewardSheetRepository.countAll()).isEqualTo(1)
        }

    @Test
    fun complete_doesNothingWhenSheetIsNotCompleted() =
        runTest {
            val sheetId =
                database.rewardSheetDao().insert(
                    rewardSheetEntity(
                        currentCount = 0,
                        goalCount = 1,
                    ),
                )

            val result = repository.complete(sheetId, GoalStampType.Heart1)

            assertThat(result).isFalse()
            assertThat(completedRewardSheetRepository.countAll()).isEqualTo(0)
        }

    @Test
    fun complete_returnsFalseWhenSheetDoesNotExist() =
        runTest {
            val result = repository.complete(999L, GoalStampType.Heart1)

            assertThat(result).isFalse()
        }
}
