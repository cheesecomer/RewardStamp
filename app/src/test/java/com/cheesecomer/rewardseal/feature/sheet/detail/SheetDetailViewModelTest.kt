package com.cheesecomer.rewardseal.feature.sheet.detail

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardseal.data.completedRewardSheet
import com.cheesecomer.rewardseal.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardseal.data.repository.RewardMilestoneRepository
import com.cheesecomer.rewardseal.data.repository.RewardSheetRepository
import com.cheesecomer.rewardseal.data.repository.RewardStampRepository
import com.cheesecomer.rewardseal.data.rewardMilestone
import com.cheesecomer.rewardseal.data.rewardSheet
import com.cheesecomer.rewardseal.data.rewardStamp
import com.cheesecomer.rewardseal.data.source.database.AppDatabase
import com.cheesecomer.rewardseal.feature.sheet.detail.SheetDetailViewModel
import com.cheesecomer.rewardseal.model.StampType
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
class SheetDetailViewModelTest {
    private val directExecutor =
        Executor { command ->
            command.run()
        }

    private lateinit var database: AppDatabase
    private lateinit var rewardSheetRepository: RewardSheetRepository
    private lateinit var completedRewardSheetRepository: CompletedRewardSheetRepository
    private lateinit var rewardStampRepository: RewardStampRepository

    private lateinit var rewardMilestoneRepository: RewardMilestoneRepository

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

        rewardStampRepository =
            RewardStampRepository(
                dao = database.rewardStampDao(),
            )

        rewardMilestoneRepository =
            RewardMilestoneRepository(
                dao = database.rewardMilestoneDao(),
            )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun load_updatesSheetAndStamps() =
        runTest {
            val sheetId =
                rewardSheetRepository.save(
                    rewardSheet(
                        id = 0,
                        title = "はみがき",
                        goalCount = 10,
                        currentCount = 1,
                    ),
                )
            completedRewardSheetRepository.save(
                completedRewardSheet(sheetId = sheetId),
            )

            rewardStampRepository.save(
                rewardStamp(
                    id = 0,
                    sheetId = sheetId,
                    position = 0,
                    stampType = StampType.Hippopotamus,
                    stampedAt = now,
                ),
            )

            rewardMilestoneRepository.saveAll(
                sheetId,
                listOf(
                    rewardMilestone(requiredSheetCount = 1),
                    rewardMilestone(requiredSheetCount = 2),
                    rewardMilestone(requiredSheetCount = 3),
                    rewardMilestone(requiredSheetCount = 4),
                    rewardMilestone(requiredSheetCount = 5),
                ),
            )

            val viewModel = createViewModel()

            viewModel.load(sheetId)

            assertThat(viewModel.uiState.sheet!!.title).isEqualTo("はみがき")
            assertThat(viewModel.uiState.stamps).hasSize(1)
            assertThat(
                viewModel.uiState.stamps
                    .single()
                    .stampType,
            ).isEqualTo(StampType.Hippopotamus)
        }

    @Test
    fun load_updatesSheetAndStampsWhenFinishedSheet() =
        runTest {
            val sheetId =
                rewardSheetRepository.save(
                    rewardSheet(
                        id = 0,
                        title = "はみがき",
                        goalCount = 10,
                        currentCount = 10,
                    ),
                )
            completedRewardSheetRepository.save(
                completedRewardSheet(sheetId = sheetId),
            )

            rewardStampRepository.save(
                rewardStamp(
                    id = 0,
                    sheetId = sheetId,
                    position = 0,
                    stampType = StampType.Hippopotamus,
                    stampedAt = now,
                ),
            )

            rewardMilestoneRepository.saveAll(
                sheetId,
                listOf(
                    rewardMilestone(requiredSheetCount = 1),
                    rewardMilestone(requiredSheetCount = 2),
                    rewardMilestone(requiredSheetCount = 3),
                    rewardMilestone(requiredSheetCount = 4),
                    rewardMilestone(requiredSheetCount = 5),
                ),
            )

            val viewModel = createViewModel()

            viewModel.load(sheetId)

            assertThat(viewModel.uiState.sheet!!.title).isEqualTo("はみがき")
            assertThat(viewModel.uiState.stamps).hasSize(1)
            assertThat(viewModel.uiState.exchangeableRewards).hasSize(2)
            assertThat(viewModel.uiState.exchangeableRewards.map { it.requiredSheetCount })
                .containsExactly(1, 2)
            assertThat(viewModel.uiState.lockedRewards).hasSize(3)
            assertThat(viewModel.uiState.lockedRewards.map { it.requiredSheetCount })
                .containsExactly(3, 4, 5)
            assertThat(
                viewModel.uiState.stamps
                    .single()
                    .stampType,
            ).isEqualTo(StampType.Hippopotamus)
        }

    @Test
    fun increment_incrementsSheetAndAddsStamp() =
        runTest {
            val sheetId =
                rewardSheetRepository.save(
                    rewardSheet(
                        id = 0,
                        currentCount = 0,
                        goalCount = 3,
                    ),
                )

            val viewModel = createViewModel()
            viewModel.load(sheetId)

            viewModel.increment(
                sheetId = sheetId,
                stampType = StampType.Hippopotamus,
            )

            val sheet = rewardSheetRepository.findById(sheetId)!!
            val stamps = rewardStampRepository.findBySheetId(sheetId)

            assertThat(sheet.currentCount).isEqualTo(1)
            assertThat(stamps).hasSize(1)
            assertThat(stamps.single().position).isEqualTo(0)
            assertThat(stamps.single().stampType).isEqualTo(StampType.Hippopotamus)
            assertThat(stamps.single().stampedAt).isEqualTo(now)
        }

    @Test
    fun increment_createsCompletedRewardSheetWhenGoalIsReached() =
        runTest {
            val sheetId =
                rewardSheetRepository.save(
                    rewardSheet(
                        id = 0,
                        title = "はみがき",
                        currentCount = 2,
                        goalCount = 3,
                    ),
                )
            rewardMilestoneRepository.saveAll(
                sheetId,
                listOf(
                    rewardMilestone(requiredSheetCount = 1),
                    rewardMilestone(requiredSheetCount = 2),
                    rewardMilestone(requiredSheetCount = 3),
                    rewardMilestone(requiredSheetCount = 4),
                    rewardMilestone(requiredSheetCount = 5),
                ),
            )

            val viewModel = createViewModel()
            viewModel.load(sheetId)

            viewModel.increment(
                sheetId = sheetId,
                stampType = StampType.Hippopotamus,
            )

            assertThat(viewModel.uiState.exchangeableRewards).hasSize(1)
            assertThat(viewModel.uiState.exchangeableRewards.map { it.requiredSheetCount })
                .containsExactly(1)
            assertThat(viewModel.uiState.lockedRewards.map { it.requiredSheetCount })
                .containsExactly(2, 3, 4, 5)

            val completedSheets = completedRewardSheetRepository.findAll()
            val restartedSheet = rewardSheetRepository.findById(sheetId)!!
            val attachedStamps =
                rewardStampRepository.findByCompletedRewardSheetId(
                    completedSheets.single().id,
                )

            assertThat(completedSheets).hasSize(1)
            assertThat(completedSheets.single().sheetId).isEqualTo(sheetId)
            assertThat(completedSheets.single().title).isEqualTo("はみがき")
            assertThat(completedSheets.single().completedAt).isEqualTo(now)

            assertThat(attachedStamps).hasSize(1)
            assertThat(restartedSheet.currentCount).isEqualTo(0)
        }

    @Test
    fun delete_deletesSheetAndCallsCallback() =
        runTest {
            val sheetId =
                rewardSheetRepository.save(
                    rewardSheet(
                        id = 0,
                        title = "はみがき",
                    ),
                )

            var completed = false

            val viewModel = createViewModel()
            viewModel.delete(sheetId) {
                completed = true
            }

            assertThat(completed).isTrue()
            assertThat(rewardSheetRepository.findAll()).isEmpty()
        }

    @Test
    fun restart_loadsSheetAgain() =
        runTest {
            val sheetId =
                rewardSheetRepository.save(
                    rewardSheet(
                        id = 0,
                        title = "はみがき",
                    ),
                )

            val viewModel = createViewModel()

            viewModel.restart(sheetId)

            assertThat(viewModel.uiState.sheet!!.title).isEqualTo("はみがき")
        }

    private fun createViewModel(): SheetDetailViewModel =
        SheetDetailViewModel(
            rewardSheetRepository = rewardSheetRepository,
            completedRewardSheetRepository = completedRewardSheetRepository,
            rewardStampRepository = rewardStampRepository,
            rewardMilestoneRepository = rewardMilestoneRepository,
            now = { now },
        )
}
