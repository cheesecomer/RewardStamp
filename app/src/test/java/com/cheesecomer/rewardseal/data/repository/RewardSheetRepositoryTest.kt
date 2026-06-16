package com.cheesecomer.rewardseal.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardseal.data.rewardSheet
import com.cheesecomer.rewardseal.data.source.database.AppDatabase
import com.cheesecomer.rewardseal.data.source.database.dao.RewardSheetDao
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class RewardSheetRepositoryTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: RewardSheetDao
    private lateinit var repository: RewardSheetRepository

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

        dao = database.rewardSheetDao()
        repository =
            RewardSheetRepository(
                dao = dao,
                now = { now },
            )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun save_insertsNewSheet() =
        runTest {
            val id =
                repository.save(
                    rewardSheet(
                        id = 0,
                        title = "はみがき",
                    ),
                )

            val result = repository.findById(id)

            assertThat(result!!.title).isEqualTo("はみがき")
        }

    @Test
    fun save_updatesExistingSheet() =
        runTest {
            val id =
                repository.save(
                    rewardSheet(
                        id = 0,
                        title = "はみがき",
                    ),
                )

            repository.save(
                rewardSheet(
                    id = id,
                    title = "おかたづけ",
                ),
            )

            val result = repository.findById(id)

            assertThat(result!!.title).isEqualTo("おかたづけ")
        }

    @Test
    fun increment_incrementsCurrentCount() =
        runTest {
            val id =
                repository.save(
                    rewardSheet(
                        id = 0,
                        currentCount = 1,
                        goalCount = 3,
                    ),
                )

            val result = repository.increment(id)

            assertThat(result).isTrue()
            assertThat(repository.findById(id)!!.currentCount).isEqualTo(2)
        }

    @Test
    fun increment_returnsFalseWhenSheetIsCompleted() =
        runTest {
            val id =
                repository.save(
                    rewardSheet(
                        id = 0,
                        currentCount = 3,
                        goalCount = 3,
                    ),
                )

            val result = repository.increment(id)

            assertThat(result).isFalse()
            assertThat(repository.findById(id)!!.currentCount).isEqualTo(3)
        }

    @Test
    fun increment_returnsFalseWhenSheetDoesNotExist() =
        runTest {
            val result = repository.increment(999L)

            assertThat(result).isFalse()
        }

    @Test
    fun restart_resetsCurrentCount() =
        runTest {
            val id =
                repository.save(
                    rewardSheet(
                        id = 0,
                        currentCount = 3,
                    ),
                )

            repository.restart(id)

            assertThat(repository.findById(id)!!.currentCount).isEqualTo(0)
        }

    @Test
    fun delete_setsDeletedAt() =
        runTest {
            val id =
                repository.save(
                    rewardSheet(
                        id = 0,
                    ),
                )

            repository.delete(id)

            val result = dao.findById(id)

            assertThat(result!!.deletedAt).isEqualTo(now)
        }

    @Test
    fun findAll_excludesDeletedSheets() =
        runTest {
            val activeId =
                repository.save(
                    rewardSheet(
                        id = 0,
                        title = "表示する",
                    ),
                )
            val deletedId =
                repository.save(
                    rewardSheet(
                        id = 0,
                        title = "表示しない",
                    ),
                )

            repository.delete(deletedId)

            val result = repository.findAll()

            assertThat(result.map { it.id }).containsExactly(activeId)
        }
}
