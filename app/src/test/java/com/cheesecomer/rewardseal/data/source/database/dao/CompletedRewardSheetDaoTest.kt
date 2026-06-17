package com.cheesecomer.rewardseal.data.source.database.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardseal.data.source.database.AppDatabase
import com.cheesecomer.rewardseal.data.source.database.completedRewardSheetEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class CompletedRewardSheetDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: CompletedRewardSheetDao

    @Before
    fun setUp() {
        database =
            Room
                .inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    AppDatabase::class.java,
                ).allowMainThreadQueries()
                .build()

        dao = database.completedRewardSheetDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun findAll_returnsSheetsOrderedByCompletedAtDesc() =
        runTest {
            dao.insert(completedRewardSheetEntity(title = "古い", completedAt = LocalDateTime.parse("2026-06-01T00:00:00")))
            dao.insert(completedRewardSheetEntity(title = "中間", completedAt = LocalDateTime.parse("2026-06-02T00:00:00")))
            dao.insert(completedRewardSheetEntity(title = "新しい", completedAt = LocalDateTime.parse("2026-06-03T00:00:00")))

            val result = dao.findAll()

            assertThat(result.map { it.title })
                .containsExactly("新しい", "中間", "古い")
                .inOrder()
        }

    @Test
    fun findById_returnsSheet() =
        runTest {
            val id =
                dao.insert(
                    completedRewardSheetEntity(title = "はみがき"),
                )

            val result = dao.findById(id)

            assertThat(result).isNotNull()
            assertThat(result!!.title).isEqualTo("はみがき")
        }

    @Test
    fun findUnreceived_returnsOnlyUnreceivedSheets() =
        runTest {
            dao.insert(completedRewardSheetEntity(title = "未受領", consumedAt = null))
            dao.insert(completedRewardSheetEntity(title = "受領済み", consumedAt = LocalDateTime.now()))

            val result = dao.findUnreceived()

            assertThat(result.map { it.title }).containsExactly("未受領")
        }

    @Test
    fun findUnreceivedBySheetId_returnsUnreceivedSheetsBySheetIdOrderedByCompletedAtAscWithLimit() =
        runTest {
            dao.insert(
                completedRewardSheetEntity(
                    sheetId = 1L,
                    title = "1番目",
                    completedAt = LocalDateTime.now(),
                    consumedAt = null,
                ),
            )
            dao.insert(
                completedRewardSheetEntity(
                    sheetId = 1L,
                    title = "2番目",
                    completedAt = LocalDateTime.now(),
                    consumedAt = null,
                ),
            )
            dao.insert(
                completedRewardSheetEntity(
                    sheetId = 1L,
                    title = "3番目",
                    completedAt = LocalDateTime.now(),
                    consumedAt = null,
                ),
            )
            dao.insert(
                completedRewardSheetEntity(
                    sheetId = 1L,
                    title = "受領済み",
                    completedAt = LocalDateTime.now(),
                    consumedAt = LocalDateTime.now(),
                ),
            )
            dao.insert(
                completedRewardSheetEntity(
                    sheetId = 2L,
                    title = "別シート",
                    completedAt = LocalDateTime.now(),
                    consumedAt = null,
                ),
            )

            val result =
                dao.findUnreceivedBySheetId(
                    sheetId = 1L,
                    take = 2,
                )

            assertThat(result.map { it.title })
                .containsExactly("1番目", "2番目")
                .inOrder()
        }

    @Test
    fun countAll_returnsAllSheetCount() =
        runTest {
            dao.insert(completedRewardSheetEntity())
            dao.insert(completedRewardSheetEntity())

            val result = dao.countAll()

            assertThat(result).isEqualTo(2)
        }

    @Test
    fun countUnreceived_returnsUnreceivedSheetCount() =
        runTest {
            dao.insert(completedRewardSheetEntity(consumedAt = null))
            dao.insert(completedRewardSheetEntity(consumedAt = null))
            dao.insert(completedRewardSheetEntity(consumedAt = LocalDateTime.now()))

            val result = dao.countUnreceived()

            assertThat(result).isEqualTo(2)
        }

    @Test
    fun countExchangeableBySheetId_returnsUnreceivedSheetCountBySheetId() =
        runTest {
            dao.insert(completedRewardSheetEntity(sheetId = 1L, consumedAt = null))
            dao.insert(completedRewardSheetEntity(sheetId = 1L, consumedAt = null))
            dao.insert(completedRewardSheetEntity(sheetId = 1L, consumedAt = LocalDateTime.now()))
            dao.insert(completedRewardSheetEntity(sheetId = 2L, consumedAt = null))

            val result = dao.countExchangeableBySheetId(sheetId = 1L)

            assertThat(result).isEqualTo(2)
        }

    @Test
    fun update_updatesSheet() =
        runTest {
            val id =
                dao.insert(
                    completedRewardSheetEntity(
                        consumedAt = null,
                    ),
                )

            val sheet = dao.findById(id)!!
            val consumedAt = LocalDateTime.parse("2026-06-16T17:07:48.429")
            dao.update(
                sheet.copy(
                    consumedAt = consumedAt,
                ),
            )

            val result = dao.findById(id)

            assertThat(result!!.consumedAt).isEqualTo(consumedAt)
        }
}
