package com.cheesecomer.rewardseal.feature.completedsheet.detail

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardseal.data.completedRewardSheet
import com.cheesecomer.rewardseal.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardseal.data.repository.RewardStampRepository
import com.cheesecomer.rewardseal.data.rewardStamp
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
class CompletedSheetDetailViewModelTest {
    private val directExecutor =
        Executor { command ->
            command.run()
        }

    private lateinit var database: AppDatabase
    private lateinit var completedRewardSheetRepository: CompletedRewardSheetRepository
    private lateinit var rewardStampRepository: RewardStampRepository

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

        completedRewardSheetRepository =
            CompletedRewardSheetRepository(
                dao = database.completedRewardSheetDao(),
            )

        rewardStampRepository =
            RewardStampRepository(
                dao = database.rewardStampDao(),
            )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun load_loadsRewardAndStamps() =
        runTest {
            val completedRewardSheetId =
                completedRewardSheetRepository.save(
                    completedRewardSheet(
                        id = 0,
                        title = "はみがき",
                        goalCount = 5,
                    ),
                )

            rewardStampRepository.save(
                rewardStamp(
                    id = 0,
                    completedRewardSheetId = completedRewardSheetId,
                    position = 0,
                    stampedAt = LocalDateTime.parse("2026-06-16T12:00:00"),
                ),
            )

            rewardStampRepository.save(
                rewardStamp(
                    id = 0,
                    completedRewardSheetId = completedRewardSheetId,
                    position = 1,
                    stampedAt = LocalDateTime.parse("2026-06-16T12:01:00"),
                ),
            )

            val viewModel = createViewModel()

            viewModel.load(completedRewardSheetId)

            assertThat(viewModel.uiState.reward).isNotNull()
            assertThat(viewModel.uiState.reward!!.title)
                .isEqualTo("はみがき")

            assertThat(viewModel.uiState.stamps)
                .hasSize(2)

            assertThat(viewModel.uiState.stamps.map { it.position })
                .containsExactly(0, 1)
                .inOrder()
        }

    private fun createViewModel(): CompletedSheetDetailViewModel =
        CompletedSheetDetailViewModel(
            completedRewardSheetRepository = completedRewardSheetRepository,
            rewardStampRepository = rewardStampRepository,
        )
}
