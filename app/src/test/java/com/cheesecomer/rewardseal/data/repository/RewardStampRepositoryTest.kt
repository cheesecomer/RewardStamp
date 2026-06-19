package com.cheesecomer.rewardseal.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardseal.data.rewardStamp
import com.cheesecomer.rewardseal.data.source.database.AppDatabase
import com.cheesecomer.rewardseal.data.source.database.dao.RewardSheetDao
import com.cheesecomer.rewardseal.data.source.database.dao.RewardStampDao
import com.cheesecomer.rewardseal.data.source.database.rewardSheetEntity
import com.cheesecomer.rewardseal.data.source.database.rewardStampEntity
import com.cheesecomer.rewardseal.model.StampType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime
import kotlin.collections.plus

@RunWith(RobolectricTestRunner::class)
class RewardStampRepositoryTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: RewardStampDao
    private lateinit var sheetDao: RewardSheetDao
    private lateinit var repository: RewardStampRepository

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
        sheetDao = database.rewardSheetDao()
        repository = RewardStampRepository(dao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun save_savesStamp() =
        runTest {
            repository.save(
                rewardStamp(
                    id = 0,
                    sheetId = 1L,
                    position = 0,
                    stampType = StampType.Shortcake,
                ),
            )

            val result = repository.findBySheetId(1L)

            assertThat(result).hasSize(1)
            assertThat(result.first().stampType).isEqualTo(StampType.Shortcake)
        }

    @Test
    fun findBySheetId_returnsStamps() =
        runTest {
            dao.insert(
                rewardStampEntity(
                    sheetId = 1L,
                    position = 0,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    sheetId = 1L,
                    position = 1,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    sheetId = 2L,
                    position = 0,
                ),
            )

            val result = repository.findBySheetId(1L)

            assertThat(result.map { it.position })
                .containsExactly(0, 1)
                .inOrder()
        }

    @Test
    fun findByCompletedRewardSheetId_returnsStamps() =
        runTest {
            dao.insert(
                rewardStampEntity(
                    completedRewardSheetId = 1L,
                    position = 0,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    completedRewardSheetId = 1L,
                    position = 1,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    completedRewardSheetId = 2L,
                    position = 0,
                ),
            )

            val result = repository.findByCompletedRewardSheetId(1L)

            assertThat(result.map { it.position })
                .containsExactly(0, 1)
                .inOrder()
        }

    @Test
    fun attachToCompletedRewardSheet_attachesStamps() =
        runTest {
            dao.insert(
                rewardStampEntity(
                    sheetId = 1L,
                    completedRewardSheetId = null,
                ),
            )
            dao.insert(
                rewardStampEntity(
                    sheetId = 1L,
                    completedRewardSheetId = null,
                ),
            )

            repository.attachToCompletedRewardSheet(
                sheetId = 1L,
                completedRewardSheetId = 10L,
            )

            assertThat(
                repository.findByCompletedRewardSheetId(10L),
            ).hasSize(2)
        }

    @Test
    fun findLatestStamp_returnsNewestStamp() =
        runTest {
            var stampIds = emptyArray<Long>()
            val sheetIds =
                arrayOf(
                    sheetDao.insert(rewardSheetEntity()).also {
                        dao.insert(
                            rewardStampEntity(
                                sheetId = it,
                                position = 0,
                            ),
                        )
                    },
                    sheetDao.insert(rewardSheetEntity()).also {
                        dao
                            .insert(
                                rewardStampEntity(
                                    sheetId = it,
                                    position = 0,
                                ),
                            ).also { stampId ->
                                stampIds = stampIds + arrayOf(stampId)
                            }
                    },
                )
            dao
                .insert(
                    rewardStampEntity(
                        sheetId = sheetIds[0],
                        completedRewardSheetId = null,
                    ),
                ).also { stampId ->
                    stampIds = stampIds + arrayOf(stampId)
                }

            val actual = repository.findLatestByEachSheet()

            assertThat(actual.keys).hasSize(2)
            assertThat(actual.keys).containsExactly(*sheetIds)
            assertThat(actual.values.map { it.id }).containsExactly(*stampIds)
        }

    @Test
    fun findLatestStamp_returnsEmpty_whenNoStampExists() =
        runTest {
            val actual = repository.findLatestByEachSheet()

            assertThat(actual.keys).hasSize(0)
        }

    @Test
    fun findLatestStamp_returnsEmpty_whenDeletedSheetOnly() =
        runTest {
            val actual = repository.findLatestByEachSheet()

            sheetDao.insert(rewardSheetEntity(deletedAt = LocalDateTime.now())).also {
                dao.insert(
                    rewardStampEntity(
                        sheetId = it,
                        position = 0,
                    ),
                )
            }

            assertThat(actual.keys).hasSize(0)
        }

    @Test
    fun findLatestStamp_ignoresCompletedSheetStamps() =
        runTest {
            sheetDao.insert(rewardSheetEntity()).also {
                dao.insert(
                    rewardStampEntity(
                        sheetId = it,
                        completedRewardSheetId = 1L,
                        position = 0,
                    ),
                )
            }

            var stampIds = emptyArray<Long>()
            val sheetIds =
                arrayOf(
                    sheetDao.insert(rewardSheetEntity()).also {
                        dao
                            .insert(
                                rewardStampEntity(
                                    sheetId = it,
                                    completedRewardSheetId = null,
                                    position = 0,
                                ),
                            ).also { sheetId -> stampIds = stampIds + arrayOf(sheetId) }
                    },
                )

            val actual = repository.findLatestByEachSheet()

            assertThat(actual.keys).hasSize(1)
            assertThat(actual.keys).containsExactly(*sheetIds)
            assertThat(actual.values.map { it.id }).containsExactly(*stampIds)
        }
}
