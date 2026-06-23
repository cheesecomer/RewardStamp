package com.cheesecomer.rewardstamp.feature.completedsheet.list

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cheesecomer.rewardstamp.data.completedRewardSheet
import com.cheesecomer.rewardstamp.data.repository.CompletedRewardSheetRepository
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
class CompletedSheetListViewModelTest {
    private val directExecutor =
        Executor { command ->
            command.run()
        }

    private lateinit var database: AppDatabase
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
    fun init_loadsCompletedSheets() =
        runTest {
            completedRewardSheetRepository.save(
                completedRewardSheet(
                    id = 0,
                    title = "はみがき",
                ),
            )
            completedRewardSheetRepository.save(
                completedRewardSheet(
                    id = 0,
                    title = "おかたづけ",
                ),
            )

            val viewModel = createViewModel()

            assertThat(viewModel.sheets.map { it.title })
                .containsExactly("はみがき", "おかたづけ")
        }

    @Test
    fun reload_updatesCompletedSheets() =
        runTest {
            val viewModel = createViewModel()

            assertThat(viewModel.sheets).isEmpty()

            completedRewardSheetRepository.save(
                completedRewardSheet(
                    id = 0,
                    title = "追加シート",
                ),
            )

            viewModel.reload()

            assertThat(viewModel.sheets.map { it.title })
                .containsExactly("追加シート")
        }

    private fun createViewModel(): CompletedSheetListViewModel =
        CompletedSheetListViewModel(
            completedRewardSheetRepository = completedRewardSheetRepository,
        )
}
