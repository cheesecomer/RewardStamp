package com.cheesecomer.rewardseal.data.source.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardseal.data.source.database.AppDatabase
import com.cheesecomer.rewardseal.data.source.database.rewardStampEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RewardStampDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: RewardStampDao

    @Before
    fun setUp() {
        database =
            Room
                .inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    AppDatabase::class.java,
                ).allowMainThreadQueries()
                .build()

        dao = database.rewardStampDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun findBySheetId_returnsUnattachedStampsOrderedByPosition() =
        runTest {
            dao.insert(
                rewardStampEntity(
                    sheetId = 1,
                    position = 2,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    sheetId = 1,
                    position = 0,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    sheetId = 1,
                    position = 1,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    sheetId = 1,
                    position = 3,
                    completedRewardSheetId = 1,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    sheetId = 2,
                    position = 0,
                ),
            )

            val result = dao.findBySheetId(1)

            assertThat(result.map { it.position })
                .containsExactly(0, 1, 2)
                .inOrder()
        }

    @Test
    fun findByCompletedRewardSheetId_returnsAttachedStampsOrderedByPosition() =
        runTest {
            dao.insert(
                rewardStampEntity(
                    completedRewardSheetId = 10,
                    position = 2,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    completedRewardSheetId = 10,
                    position = 0,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    completedRewardSheetId = 10,
                    position = 1,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    completedRewardSheetId = 20,
                    position = 0,
                ),
            )

            val result = dao.findByCompletedRewardSheetId(10)

            assertThat(result.map { it.position })
                .containsExactly(0, 1, 2)
                .inOrder()
        }

    @Test
    fun deleteBySheetId_deletesOnlyTargetSheet() =
        runTest {
            dao.insert(
                rewardStampEntity(
                    sheetId = 1,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    sheetId = 1,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    sheetId = 2,
                ),
            )

            dao.deleteBySheetId(1)

            assertThat(dao.findBySheetId(1)).isEmpty()
            assertThat(dao.findBySheetId(2)).hasSize(1)
        }

    @Test
    fun attachToCompletedRewardSheet_updatesOnlyUnattachedStamps() =
        runTest {
            dao.insert(
                rewardStampEntity(
                    sheetId = 1,
                    completedRewardSheetId = null,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    sheetId = 1,
                    completedRewardSheetId = null,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    sheetId = 1,
                    completedRewardSheetId = 99,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    sheetId = 2,
                    completedRewardSheetId = null,
                ),
            )

            dao.attachToCompletedRewardSheet(
                sheetId = 1,
                completedRewardSheetId = 10,
            )

            assertThat(dao.findByCompletedRewardSheetId(10)).hasSize(2)
            assertThat(dao.findByCompletedRewardSheetId(99)).hasSize(1)
            assertThat(dao.findBySheetId(2)).hasSize(1)
        }
}
