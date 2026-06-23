package com.cheesecomer.rewardstamp.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardstamp.data.source.database.AppDatabase
import com.cheesecomer.rewardstamp.data.source.database.completedRewardSheetEntity
import com.cheesecomer.rewardstamp.data.source.database.rewardMilestoneEntity
import com.cheesecomer.rewardstamp.data.source.database.rewardSheetEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class ExchangeableRewardRepositoryTest {
    private lateinit var database: AppDatabase
    private lateinit var repository: ExchangeableSheetRepository
    private lateinit var completedRewardSheetRepository: CompletedRewardSheetRepository

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

        val rewardSheetRepository =
            RewardSheetRepository(
                dao = database.rewardSheetDao(),
            )

        completedRewardSheetRepository =
            CompletedRewardSheetRepository(
                dao = database.completedRewardSheetDao(),
                now = { now },
            )

        val rewardMilestoneRepository =
            RewardMilestoneRepository(
                dao = database.rewardMilestoneDao(),
            )

        repository =
            ExchangeableSheetRepository(
                rewardSheetRepository = rewardSheetRepository,
                completedRewardSheetRepository = completedRewardSheetRepository,
                rewardMilestoneRepository = rewardMilestoneRepository,
            )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun findAll_returnsExchangeableSheetsWithMilestones() =
        runTest {
            val sheetId =
                database.rewardSheetDao().insert(
                    rewardSheetEntity(
                        title = "はみがき",
                    ),
                )

            database.completedRewardSheetDao().insert(
                completedRewardSheetEntity(
                    sheetId = sheetId,
                    consumedAt = null,
                ),
            )
            database.completedRewardSheetDao().insert(
                completedRewardSheetEntity(
                    sheetId = sheetId,
                    consumedAt = null,
                ),
            )

            database.rewardMilestoneDao().insert(
                rewardMilestoneEntity(
                    sheetId = sheetId,
                    requiredSheetCount = 1,
                    reward = "シール",
                ),
            )
            database.rewardMilestoneDao().insert(
                rewardMilestoneEntity(
                    sheetId = sheetId,
                    requiredSheetCount = 3,
                    reward = "アイス",
                ),
            )

            val result = repository.findAll()

            assertThat(result).hasSize(1)

            val sheet = result.single()
            assertThat(sheet.rewardSheet.id).isEqualTo(sheetId)
            assertThat(sheet.rewardSheet.title).isEqualTo("はみがき")
            assertThat(sheet.exchangeableSheetCount).isEqualTo(2)
            assertThat(sheet.exchangeableMilestones.map { it.reward })
                .containsExactly("シール")
            assertThat(sheet.closestMilestone!!.reward).isEqualTo("アイス")
        }

    @Test
    fun exchangeReward_marksRewardsAsReceived() =
        runTest {
            val sheetId =
                database.rewardSheetDao().insert(
                    rewardSheetEntity(),
                )

            database.completedRewardSheetDao().insert(
                completedRewardSheetEntity(
                    sheetId = sheetId,
                    completedAt = LocalDateTime.parse("2026-06-01T00:00:00"),
                    consumedAt = null,
                ),
            )
            database.completedRewardSheetDao().insert(
                completedRewardSheetEntity(
                    sheetId = sheetId,
                    completedAt = LocalDateTime.parse("2026-06-02T00:00:00"),
                    consumedAt = null,
                ),
            )
            database.completedRewardSheetDao().insert(
                completedRewardSheetEntity(
                    sheetId = sheetId,
                    completedAt = LocalDateTime.parse("2026-06-03T00:00:00"),
                    consumedAt = null,
                ),
            )

            repository.exchangeReward(
                sheetId = sheetId,
                exchangeCount = 2,
            )

            val remaining =
                database.completedRewardSheetDao().findUnreceivedBySheetId(
                    sheetId = sheetId,
                    take = 10,
                )

            assertThat(remaining).hasSize(1)
            assertThat(remaining.single().completedAt)
                .isEqualTo(LocalDateTime.parse("2026-06-03T00:00:00"))
        }
}
