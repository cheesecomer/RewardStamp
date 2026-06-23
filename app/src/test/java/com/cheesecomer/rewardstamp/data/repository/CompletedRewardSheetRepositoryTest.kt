package com.cheesecomer.rewardstamp.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardstamp.data.source.database.AppDatabase
import com.cheesecomer.rewardstamp.data.source.database.completedRewardSheetEntity
import com.cheesecomer.rewardstamp.data.source.database.dao.CompletedRewardSheetDao
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class CompletedRewardSheetRepositoryTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: CompletedRewardSheetDao
    private lateinit var repository: CompletedRewardSheetRepository

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

        dao = database.completedRewardSheetDao()

        repository =
            CompletedRewardSheetRepository(
                dao = dao,
                now = { now },
            )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun markRewardReceived_marksSpecifiedNumberOfUnreceivedSheetsAsConsumed() =
        runTest {
            dao.insert(completedRewardSheetEntity(sheetId = 1L, completedAt = LocalDateTime.parse("2026-06-01T00:00:00")))
            dao.insert(completedRewardSheetEntity(sheetId = 1L, completedAt = LocalDateTime.parse("2026-06-02T00:00:00")))
            dao.insert(completedRewardSheetEntity(sheetId = 1L, completedAt = LocalDateTime.parse("2026-06-03T00:00:00")))
            dao.insert(completedRewardSheetEntity(sheetId = 2L, completedAt = LocalDateTime.parse("2026-06-04T00:00:00")))

            repository.markRewardReceived(
                sheetId = 1L,
                exchangeCount = 2,
            )

            val sheet1Rows = dao.findUnreceivedBySheetId(sheetId = 1L, take = 10)
            val sheet2Rows = dao.findUnreceivedBySheetId(sheetId = 2L, take = 10)

            assertThat(sheet1Rows).hasSize(1)
            assertThat(sheet1Rows.first().completedAt)
                .isEqualTo(LocalDateTime.parse("2026-06-03T00:00:00"))

            assertThat(sheet2Rows).hasSize(1)
            val all = dao.findAll()

            val consumed =
                all.filter {
                    it.sheetId == 1L &&
                        it.consumedAt != null
                }

            assertThat(consumed).hasSize(2)
            assertThat(consumed.all { it.consumedAt == now }).isTrue()
        }
}
