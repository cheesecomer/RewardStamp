package com.cheesecomer.rewardseal.data.source.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardseal.data.source.database.AppDatabase
import com.cheesecomer.rewardseal.data.source.database.rewardMilestoneEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RewardMilestoneDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: RewardMilestoneDao

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
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun findBySheetId_returnsMilestonesOrderedByRequiredCompletionsAsc() =
        runTest {
            dao.insert(rewardMilestoneEntity(sheetId = 1L, reward = "3回", requiredCompletions = 3))
            dao.insert(rewardMilestoneEntity(sheetId = 1L, reward = "1回", requiredCompletions = 1))
            dao.insert(rewardMilestoneEntity(sheetId = 1L, reward = "2回", requiredCompletions = 2))
            dao.insert(rewardMilestoneEntity(sheetId = 2L, reward = "別シート", requiredCompletions = 1))

            val result = dao.findBySheetId(sheetId = 1L)

            assertThat(result.map { it.reward })
                .containsExactly("1回", "2回", "3回")
                .inOrder()
        }

    @Test
    fun findExchangeableBySheetId_returnsMilestonesThatRequiredCompletionsAreReached() =
        runTest {
            dao.insert(rewardMilestoneEntity(sheetId = 1L, reward = "1回", requiredCompletions = 1))
            dao.insert(rewardMilestoneEntity(sheetId = 1L, reward = "2回", requiredCompletions = 2))
            dao.insert(rewardMilestoneEntity(sheetId = 1L, reward = "3回", requiredCompletions = 3))
            dao.insert(rewardMilestoneEntity(sheetId = 2L, reward = "別シート", requiredCompletions = 1))

            val result =
                dao.findExchangeableBySheetId(
                    sheetId = 1L,
                    requiredCompletions = 2,
                )

            assertThat(result.map { it.reward })
                .containsExactly("1回", "2回")
                .inOrder()
        }

    @Test
    fun findNext_returnsFirstMilestoneThatRequiredCompletionsIsNotReached() =
        runTest {
            dao.insert(rewardMilestoneEntity(sheetId = 1L, reward = "1回", requiredCompletions = 1))
            dao.insert(rewardMilestoneEntity(sheetId = 1L, reward = "3回", requiredCompletions = 3))
            dao.insert(rewardMilestoneEntity(sheetId = 1L, reward = "5回", requiredCompletions = 5))
            dao.insert(rewardMilestoneEntity(sheetId = 2L, reward = "別シート", requiredCompletions = 2))

            val result =
                dao.findNext(
                    sheetId = 1L,
                    requiredCompletions = 3,
                )

            assertThat(result).isNotNull()
            assertThat(result!!.reward).isEqualTo("5回")
        }

    @Test
    fun findNext_returnsNullWhenAllMilestonesAreReached() =
        runTest {
            dao.insert(rewardMilestoneEntity(sheetId = 1L, reward = "1回", requiredCompletions = 1))
            dao.insert(rewardMilestoneEntity(sheetId = 1L, reward = "2回", requiredCompletions = 2))

            val result =
                dao.findNext(
                    sheetId = 1L,
                    requiredCompletions = 2,
                )

            assertThat(result).isNull()
        }

    @Test
    fun update_updatesMilestone() =
        runTest {
            val id =
                dao.insert(
                    rewardMilestoneEntity(
                        reward = "アイス",
                    ),
                )

            val milestone =
                dao
                    .findBySheetId(sheetId = 1L)
                    .first { it.id == id }

            dao.update(
                milestone.copy(
                    reward = "チョコ",
                ),
            )

            val result =
                dao
                    .findBySheetId(sheetId = 1L)
                    .first { it.id == id }

            assertThat(result.reward).isEqualTo("チョコ")
        }

    @Test
    fun deleteNotIn_deletesMilestonesNotIncludedInIdsForSheet() =
        runTest {
            val keepId =
                dao.insert(
                    rewardMilestoneEntity(
                        sheetId = 1L,
                        reward = "残す",
                    ),
                )
            dao.insert(
                rewardMilestoneEntity(
                    sheetId = 1L,
                    reward = "消す",
                ),
            )
            dao.insert(
                rewardMilestoneEntity(
                    sheetId = 2L,
                    reward = "別シート",
                ),
            )

            dao.deleteNotIn(
                sheetId = 1L,
                ids = listOf(keepId),
            )

            val sheet1Result = dao.findBySheetId(sheetId = 1L)
            val sheet2Result = dao.findBySheetId(sheetId = 2L)

            assertThat(sheet1Result.map { it.reward }).containsExactly("残す")
            assertThat(sheet2Result.map { it.reward }).containsExactly("別シート")
        }

    @Test
    fun delete_deletesMilestone() =
        runTest {
            val id =
                dao.insert(
                    rewardMilestoneEntity(
                        reward = "消す",
                    ),
                )

            val milestone =
                dao
                    .findBySheetId(sheetId = 1L)
                    .first { it.id == id }

            dao.delete(milestone)

            val result = dao.findBySheetId(sheetId = 1L)

            assertThat(result).isEmpty()
        }
}
