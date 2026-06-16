package com.cheesecomer.rewardseal.feature.exchangeablereward.list

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardseal.data.completedRewardSheet
import com.cheesecomer.rewardseal.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardseal.data.repository.ExchangeableRewardRepository
import com.cheesecomer.rewardseal.data.repository.RewardMilestoneRepository
import com.cheesecomer.rewardseal.data.repository.RewardSheetRepository
import com.cheesecomer.rewardseal.data.rewardMilestone
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
class ExchangeableRewardListViewModelTest {
    private val directExecutor =
        Executor { command ->
            command.run()
        }

    private lateinit var database: AppDatabase
    private lateinit var rewardSheetRepository: RewardSheetRepository
    private lateinit var completedRewardSheetRepository: CompletedRewardSheetRepository
    private lateinit var rewardMilestoneRepository: RewardMilestoneRepository
    private lateinit var exchangeableRewardRepository: ExchangeableRewardRepository

    private val now = LocalDateTime.parse("2026-06-16T12:00:00")

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
                now = { now },
            )

        rewardMilestoneRepository =
            RewardMilestoneRepository(
                dao = database.rewardMilestoneDao(),
            )

        exchangeableRewardRepository =
            ExchangeableRewardRepository(
                rewardSheetRepository = rewardSheetRepository,
                completedRewardSheetRepository = completedRewardSheetRepository,
                rewardMilestoneRepository = rewardMilestoneRepository,
            )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun init_loadsExchangeableRewards() =
        runTest {
            val sheetId = createExchangeableSheet()

            val viewModel = createViewModel()

            assertThat(viewModel.uiState.sheets).hasSize(1)

            val sheet = viewModel.uiState.sheets.single()
            assertThat(sheet.id).isEqualTo(sheetId)
            assertThat(sheet.title).isEqualTo("はみがき")
            assertThat(sheet.unconsumedCompletedCount).isEqualTo(1)
            assertThat(sheet.exchangeableMilestones.map { it.reward })
                .containsExactly("アイス")
            assertThat(sheet.nextMilestone).isNull()
        }

    @Test
    fun reload_updatesExchangeableRewards() =
        runTest {
            val viewModel = createViewModel()

            assertThat(viewModel.uiState.sheets).isEmpty()

            val sheetId = createExchangeableSheet()

            viewModel.reload()

            assertThat(viewModel.uiState.sheets).hasSize(1)
            assertThat(
                viewModel.uiState.sheets
                    .single()
                    .id,
            ).isEqualTo(sheetId)
        }

    @Test
    fun receiveReward_exchangesRewardReloadsAndCallsCallback() =
        runTest {
            val sheetId = createExchangeableSheet()

            val viewModel = createViewModel()

            var completed = false

            viewModel.receiveReward(
                id = sheetId,
                milestone =
                    rewardMilestone(
                        sheetId = sheetId,
                        requiredCompletions = 1,
                        reward = "アイス",
                    ),
            ) {
                completed = true
            }

            assertThat(completed).isTrue()
            assertThat(viewModel.uiState.sheets).isEmpty()
            assertThat(
                completedRewardSheetRepository.countExchangeableBySheetId(sheetId),
            ).isEqualTo(0)
        }

    private suspend fun createExchangeableSheet(): Long {
        val sheetId =
            rewardSheetRepository.save(
                rewardSheet(
                    id = 0,
                    title = "はみがき",
                ),
            )

        completedRewardSheetRepository.save(
            completedRewardSheet(
                id = 0,
                sheetId = sheetId,
                title = "はみがき",
                consumedAt = null,
            ),
        )

        rewardMilestoneRepository.saveAll(
            sheetId = sheetId,
            rewardMilestones =
                listOf(
                    rewardMilestone(
                        id = 0,
                        sheetId = sheetId,
                        requiredCompletions = 1,
                        reward = "アイス",
                    ),
                ),
        )

        return sheetId
    }

    private fun createViewModel(): ExchangeableRewardListViewModel =
        ExchangeableRewardListViewModel(
            exchangeableRewardRepository = exchangeableRewardRepository,
        )
}
