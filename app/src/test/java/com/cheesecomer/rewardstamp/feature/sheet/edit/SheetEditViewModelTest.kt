package com.cheesecomer.rewardstamp.feature.sheet.edit

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardstamp.data.repository.RewardMilestoneRepository
import com.cheesecomer.rewardstamp.data.repository.RewardSheetRepository
import com.cheesecomer.rewardstamp.data.rewardMilestone
import com.cheesecomer.rewardstamp.data.rewardSheet
import com.cheesecomer.rewardstamp.data.source.database.AppDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.Executor

@RunWith(RobolectricTestRunner::class)
class SheetEditViewModelTest {
    private val directExecutor =
        Executor { command ->
            command.run()
        }

    private lateinit var database: AppDatabase
    private lateinit var rewardSheetRepository: RewardSheetRepository
    private lateinit var milestoneRepository: RewardMilestoneRepository

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

        milestoneRepository =
            RewardMilestoneRepository(
                dao = database.rewardMilestoneDao(),
            )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun updateTitle_updatesTitle() =
        runTest {
            val viewModel = createViewModel()

            viewModel.updateTitle("はみがき")

            assertThat(viewModel.uiState.title).isEqualTo("はみがき")
        }

    @Test
    fun incrementGoalCount_incrementsGoalCount() =
        runTest {
            val viewModel = createViewModel()

            viewModel.incrementGoalCount()

            assertThat(viewModel.uiState.goalCount).isEqualTo(11)
        }

    @Test
    fun decrementGoalCount_decrementsGoalCount() =
        runTest {
            val viewModel = createViewModel()

            viewModel.decrementGoalCount()

            assertThat(viewModel.uiState.goalCount).isEqualTo(9)
        }

    @Test
    fun decrementGoalCount_doesNotGoBelowOne() =
        runTest {
            val viewModel = createViewModel()

            repeat(20) {
                viewModel.decrementGoalCount()
            }

            assertThat(viewModel.uiState.goalCount).isEqualTo(1)
        }

    @Test
    fun addMilestone_addsMilestone() =
        runTest {
            val viewModel = createViewModel()
            viewModel.createMilestone(rewardMilestone())

            assertThat(viewModel.uiState.milestones).hasSize(1)
        }

    @Test
    fun removeMilestone_removesMilestone() =
        runTest {
            val viewModel = createViewModel()
            viewModel.createMilestone(rewardMilestone())
            viewModel.createMilestone(rewardMilestone())

            viewModel.removeMilestone(0)

            assertThat(viewModel.uiState.milestones).hasSize(1)
        }

    @Test
    fun updateMilestone_updatesRequiredSheetCount() =
        runTest {
            val viewModel = createViewModel()
            val milestone = rewardMilestone()

            viewModel.createMilestone(milestone)
            viewModel.updateMilestone(
                index = 0,
                value = milestone.copy(requiredSheetCount = 3, reward = "アイス"),
            )

            assertThat(viewModel.uiState.milestones[0].requiredSheetCount)
                .isEqualTo(3)
            assertThat(viewModel.uiState.milestones[0].reward)
                .isEqualTo("アイス")
        }

    @Test
    fun load_updatesUiState() =
        runTest {
            val sheetId =
                rewardSheetRepository.save(
                    rewardSheet(
                        id = 0,
                        title = "はみがき",
                        goalCount = 5,
                    ),
                )

            milestoneRepository.saveAll(
                sheetId = sheetId,
                rewardMilestones =
                    listOf(
                        rewardMilestone(
                            id = 0,
                            sheetId = sheetId,
                            requiredSheetCount = 1,
                            reward = "シール",
                        ),
                        rewardMilestone(
                            id = 0,
                            sheetId = sheetId,
                            requiredSheetCount = 3,
                            reward = "アイス",
                        ),
                    ),
            )

            val viewModel = createViewModel()

            viewModel.load(sheetId)

            assertThat(viewModel.uiState.sheetId).isEqualTo(sheetId)
            assertThat(viewModel.uiState.title).isEqualTo("はみがき")
            assertThat(viewModel.uiState.goalCount).isEqualTo(5)
            assertThat(viewModel.uiState.milestones.map { it.reward })
                .containsExactly("シール", "アイス")
                .inOrder()
        }

    @Test
    fun createRewardSheet_createsSheetFromUiState() =
        runTest {
            val viewModel = createViewModel()

            viewModel.updateTitle("はみがき")
            viewModel.incrementGoalCount()

            val sheet = viewModel.createRewardSheet()

            assertThat(sheet.title).isEqualTo("はみがき")
            assertThat(sheet.currentCount).isEqualTo(0)
            assertThat(sheet.goalCount).isEqualTo(11)
        }

    @Test
    fun canSave_returnsTrueWhenUiStateIsValid() =
        runTest {
            val viewModel = createViewModel()

            viewModel.updateTitle("はみがき")
            viewModel.createMilestone(rewardMilestone())

            assertThat(viewModel.canSave()).isTrue()
        }

    @Test
    fun canSave_returnsFalseWhenTitleIsBlank() =
        runTest {
            val viewModel = createViewModel()
            viewModel.createMilestone(rewardMilestone())
            viewModel.updateTitle("")

            assertThat(viewModel.canSave()).isFalse()
        }

    @Test
    fun canSave_returnsFalseWhenMilestonesIsEmpty() =
        runTest {
            val viewModel = createViewModel()
            viewModel.updateTitle("はみがき")

            assertThat(viewModel.canSave()).isFalse()
        }

    @Test
    fun save_doesNotSaveWhenUiStateIsInvalid() =
        runTest {
            val viewModel = createViewModel()
            var saved = false

            viewModel.save {
                saved = true
            }

            assertThat(saved).isFalse()
            assertThat(rewardSheetRepository.findAll()).isEmpty()
        }

    @Test
    fun save_savesSheetAndMilestonesWhenUiStateIsValid() =
        runTest {
            val viewModel = createViewModel()

            viewModel.updateTitle("はみがき")
            viewModel.createMilestone(
                rewardMilestone(
                    requiredSheetCount = 3,
                    reward = "アイス",
                ),
            )

            var saved = false

            viewModel.save {
                saved = true
            }

            val sheets = rewardSheetRepository.findAll()
            val savedSheet = sheets.single()
            val milestones = milestoneRepository.findBySheetId(savedSheet.id)

            assertThat(saved).isTrue()
            assertThat(savedSheet.title).isEqualTo("はみがき")
            assertThat(savedSheet.goalCount).isEqualTo(10)
            assertThat(milestones).hasSize(1)
            assertThat(milestones.single().requiredSheetCount).isEqualTo(3)
            assertThat(milestones.single().reward).isEqualTo("アイス")
        }

    private fun createViewModel(): SheetEditViewModel =
        SheetEditViewModel(
            rewardSheetRepository = rewardSheetRepository,
            milestoneRepository = milestoneRepository,
        )
}
