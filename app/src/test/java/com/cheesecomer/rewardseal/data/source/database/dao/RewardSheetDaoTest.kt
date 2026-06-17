package com.cheesecomer.rewardseal.data.source.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardseal.data.source.database.AppDatabase
import com.cheesecomer.rewardseal.data.source.database.completedRewardSheetEntity
import com.cheesecomer.rewardseal.data.source.database.rewardMilestoneEntity
import com.cheesecomer.rewardseal.data.source.database.rewardSheetEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class RewardSheetDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var rewardSheetDao: RewardSheetDao
    private lateinit var completedRewardSheetDao: CompletedRewardSheetDao
    private lateinit var rewardMilestoneDao: RewardMilestoneDao

    @Before
    fun setUp() {
        database =
            Room
                .inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    AppDatabase::class.java,
                ).allowMainThreadQueries()
                .build()

        rewardSheetDao = database.rewardSheetDao()
        completedRewardSheetDao = database.completedRewardSheetDao()
        rewardMilestoneDao = database.rewardMilestoneDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun findAll_returnsOnlyNonDeletedSheets() =
        runTest {
            rewardSheetDao.insert(
                rewardSheetEntity(
                    title = "はみがき",
                ),
            )
            rewardSheetDao.insert(
                rewardSheetEntity(
                    title = "おかたづけ",
                    deletedAt = LocalDateTime.now(),
                ),
            )

            val result = rewardSheetDao.findAll()

            assertThat(result).hasSize(1)
            assertThat(result.first().title).isEqualTo("はみがき")
        }

    @Test
    fun findById_returnsInsertedSheet() =
        runTest {
            val id =
                rewardSheetDao.insert(
                    rewardSheetEntity(
                        title = "はみがき",
                    ),
                )

            val result = rewardSheetDao.findById(id)

            assertThat(result).isNotNull()
            assertThat(result!!.title).isEqualTo("はみがき")
        }

    @Test
    fun update_updatesSheet() =
        runTest {
            val id = rewardSheetDao.insert(rewardSheetEntity())

            val sheet = rewardSheetDao.findById(id)!!
            rewardSheetDao.update(
                sheet.copy(
                    title = "更新後",
                ),
            )

            val result = rewardSheetDao.findById(id)

            assertThat(result!!.title).isEqualTo("更新後")
        }

    @Test
    fun delete_deletesSheet() =
        runTest {
            val id = rewardSheetDao.insert(rewardSheetEntity())

            rewardSheetDao.delete(
                rewardSheetDao.findById(id)!!,
            )

            assertThat(
                rewardSheetDao.findById(id),
            ).isNull()
        }

    @Test
    fun findExchangeable_returnsOnlyExchangeableSheets() =
        runTest {
            val exchangeableId =
                rewardSheetDao.insert(
                    rewardSheetEntity(
                        title = "交換可能",
                    ),
                )
            rewardMilestoneDao.insert(
                rewardMilestoneEntity(sheetId = exchangeableId, requiredCompletions = 1),
            )

            val unexchangeableId =
                rewardSheetDao.insert(
                    rewardSheetEntity(
                        title = "交換不可",
                    ),
                )
            rewardMilestoneDao.insert(
                rewardMilestoneEntity(sheetId = unexchangeableId, requiredCompletions = 2),
            )

            val consumedId =
                rewardSheetDao.insert(
                    rewardSheetEntity(
                        title = "交換済み",
                    ),
                )
            rewardMilestoneDao.insert(
                rewardMilestoneEntity(sheetId = consumedId, requiredCompletions = 1),
            )

            completedRewardSheetDao.insert(
                completedRewardSheetEntity(
                    sheetId = exchangeableId,
                    consumedAt = null,
                ),
            )

            completedRewardSheetDao.insert(
                completedRewardSheetEntity(
                    sheetId = unexchangeableId,
                    consumedAt = null,
                ),
            )

            completedRewardSheetDao.insert(
                completedRewardSheetEntity(
                    sheetId = consumedId,
                    consumedAt = LocalDateTime.now(),
                ),
            )

            val result = rewardSheetDao.findExchangeable()

            assertThat(result.map { it.title })
                .containsExactly("交換可能")
        }

    @Test
    fun countExchangeable_returnsNumberOfExchangeableSheets() =
        runTest {
            val id =
                rewardSheetDao.insert(
                    rewardSheetEntity(),
                )

            completedRewardSheetDao.insert(
                completedRewardSheetEntity(
                    sheetId = id,
                ),
            )

            val count = rewardSheetDao.countExchangeable()

            assertThat(count).isEqualTo(1)
        }
}
