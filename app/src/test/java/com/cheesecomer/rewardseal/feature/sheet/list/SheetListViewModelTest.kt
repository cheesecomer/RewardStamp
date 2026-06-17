package com.cheesecomer.rewardseal.feature.sheet.list

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardseal.data.completedRewardSheet
import com.cheesecomer.rewardseal.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardseal.data.repository.RewardSheetRepository
import com.cheesecomer.rewardseal.data.rewardSheet
import com.cheesecomer.rewardseal.data.source.database.AppDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime
import java.util.concurrent.Executor

@RunWith(RobolectricTestRunner::class)
class SheetListViewModelTest {
    private val directExecutor =
        Executor { command ->
            command.run()
        }
    private lateinit var database: AppDatabase
    private lateinit var rewardSheetRepository: RewardSheetRepository
    private lateinit var completedRewardSheetRepository: CompletedRewardSheetRepository

    @Before
    fun setUp() {
        database =
            Room
                .inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext(),
                    AppDatabase::class.java,
                ).allowMainThreadQueries()
                .setQueryExecutor(directExecutor)
                .setTransactionExecutor(directExecutor)
                .build()

        rewardSheetRepository =
            RewardSheetRepository(
                dao = database.rewardSheetDao(),
            )

        completedRewardSheetRepository =
            CompletedRewardSheetRepository(
                dao = database.completedRewardSheetDao(),
            )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun init_loadsSheetsAndCounts() =
        runTest {
            rewardSheetRepository.save(
                rewardSheet(
                    id = 0,
                    title = "はみがき",
                ),
            )

            val exchangeableSheetId =
                rewardSheetRepository.save(
                    rewardSheet(
                        id = 0,
                        title = "おかたづけ",
                    ),
                )

            completedRewardSheetRepository.save(
                completedRewardSheet(
                    id = 0,
                    sheetId = exchangeableSheetId,
                    title = "おかたづけ",
                    consumedAt = null,
                ),
            )

            completedRewardSheetRepository.save(
                completedRewardSheet(
                    id = 0,
                    sheetId = exchangeableSheetId,
                    title = "おかたづけ",
                    consumedAt = LocalDateTime.parse("2026-06-16T12:00:00"),
                ),
            )

            val viewModel =
                SheetListViewModel(
                    rewardSheetRepository = rewardSheetRepository,
                    completedRewardSheetRepository = completedRewardSheetRepository,
                )

            assertThat(viewModel.sheets.map { it.title })
                .containsExactly("はみがき", "おかたづけ")
            assertThat(viewModel.exchangeableSheetCount).isEqualTo(1)
            assertThat(viewModel.completedSheetCount).isEqualTo(2)
        }

    @Test
    fun reload_updatesSheetsAndCounts() =
        runTest {
            val viewModel =
                SheetListViewModel(
                    rewardSheetRepository = rewardSheetRepository,
                    completedRewardSheetRepository = completedRewardSheetRepository,
                )

            val sheetId =
                rewardSheetRepository.save(
                    rewardSheet(
                        id = 0,
                        title = "追加シート",
                    ),
                )

            completedRewardSheetRepository.save(
                completedRewardSheet(
                    id = 0,
                    sheetId = sheetId,
                    title = "追加シート",
                    consumedAt = null,
                ),
            )

            viewModel.reload()

            assertThat(viewModel.sheets.map { it.title })
                .containsExactly("追加シート")
            assertThat(viewModel.exchangeableSheetCount).isEqualTo(1)
            assertThat(viewModel.completedSheetCount).isEqualTo(1)
        }
}
