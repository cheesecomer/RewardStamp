package com.cheesecomer.rewardstamp.feature.sheet.edit

import android.os.Looper
import com.cheesecomer.rewardstamp.data.repository.RewardMilestoneRepository
import com.cheesecomer.rewardstamp.data.repository.RewardSheetRepository
import com.cheesecomer.rewardstamp.data.rewardMilestone
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class SheetEditViewModelDoubleSubmitTest {
    @Test
    fun save_ignoresSecondCallWhileSaving() =
        runTest {
            val gate = CompletableDeferred<Unit>()
            val rewardSheetRepository =
                mock<RewardSheetRepository> {
                    onBlocking { save(any()) } doSuspendableAnswer {
                        gate.await()
                        1L
                    }
                }
            val milestoneRepository = mock<RewardMilestoneRepository>()
            val viewModel = SheetEditViewModel(rewardSheetRepository, milestoneRepository)
            viewModel.updateTitle("はみがき")
            viewModel.createMilestone(rewardMilestone())

            var savedCount = 0
            viewModel.save { savedCount++ }
            viewModel.save { savedCount++ }
            gate.complete(Unit)
            shadowOf(Looper.getMainLooper()).idle()

            verifyBlocking(rewardSheetRepository, times(1)) { save(any()) }
            assertThat(savedCount).isEqualTo(1)
        }
}
