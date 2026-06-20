package com.cheesecomer.rewardseal.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardseal.data.rewardMilestone
import com.cheesecomer.rewardseal.data.source.database.AppDatabase
import com.cheesecomer.rewardseal.data.source.database.dao.RewardMilestoneDao
import com.cheesecomer.rewardseal.data.source.database.rewardMilestoneEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RewardMilestoneRepositoryTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: RewardMilestoneDao
    private lateinit var repository: RewardMilestoneRepository

    @Before
    fun setUp() {
        database =
            Room
                .inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    AppDatabase::class.java,
                ).allowMainThreadQueries()
                .build()

        dao = database.rewardMilestoneDao()
        repository = RewardMilestoneRepository(dao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun saveAll_deletesMilestonesRemovedFromScreen() =
        runTest {
            val keepId =
                dao.insert(
                    rewardMilestoneEntity(
                        sheetId = 1L,
                        requiredSheetCount = 1,
                        reward = "残す",
                    ),
                )
            dao.insert(
                rewardMilestoneEntity(
                    sheetId = 1L,
                    requiredSheetCount = 2,
                    reward = "消す",
                ),
            )

            repository.saveAll(
                sheetId = 1L,
                rewardMilestones =
                    listOf(
                        rewardMilestone(
                            id = keepId,
                            sheetId = 1L,
                            requiredSheetCount = 1,
                            reward = "残す",
                        ),
                    ),
            )

            val result = repository.findBySheetId(1L)

            assertThat(result.map { it.reward }).containsExactly("残す")
        }

    @Test
    fun saveAll_updatesExistingMilestonesWithoutChangingId() =
        runTest {
            val id =
                dao.insert(
                    rewardMilestoneEntity(
                        sheetId = 1L,
                        requiredSheetCount = 1,
                        reward = "変更前",
                    ),
                )

            repository.saveAll(
                sheetId = 1L,
                rewardMilestones =
                    listOf(
                        rewardMilestone(
                            id = id,
                            sheetId = 1L,
                            requiredSheetCount = 3,
                            reward = "変更後",
                        ),
                    ),
            )

            val result = repository.findBySheetId(1L).single()

            assertThat(result.id).isEqualTo(id)
            assertThat(result.requiredSheetCount).isEqualTo(3)
            assertThat(result.reward).isEqualTo("変更後")
        }

    @Test
    fun saveAll_insertsNewMilestones() =
        runTest {
            repository.saveAll(
                sheetId = 1L,
                rewardMilestones =
                    listOf(
                        rewardMilestone(
                            id = 0L,
                            sheetId = 1L,
                            requiredSheetCount = 1,
                            reward = "新規",
                        ),
                    ),
            )

            val result = repository.findBySheetId(1L)

            assertThat(result).hasSize(1)
            assertThat(result.single().id).isNotEqualTo(0L)
            assertThat(result.single().reward).isEqualTo("新規")
        }

    @Test
    fun saveAll_doesNotDeleteMilestonesForOtherSheets() =
        runTest {
            val keepId =
                dao.insert(
                    rewardMilestoneEntity(
                        sheetId = 1L,
                        reward = "対象シート",
                    ),
                )
            dao.insert(
                rewardMilestoneEntity(
                    sheetId = 2L,
                    reward = "別シート",
                ),
            )

            repository.saveAll(
                sheetId = 1L,
                rewardMilestones =
                    listOf(
                        rewardMilestone(
                            id = keepId,
                            sheetId = 1L,
                            reward = "対象シート更新",
                        ),
                    ),
            )

            val sheet1 = repository.findBySheetId(1L)
            val sheet2 = repository.findBySheetId(2L)

            assertThat(sheet1.map { it.reward }).containsExactly("対象シート更新")
            assertThat(sheet2.map { it.reward }).containsExactly("別シート")
        }
}
