package com.cheesecomer.rewardstamp.feature.sheet.detail

import android.os.Looper
import com.cheesecomer.rewardstamp.data.repository.CompletedRewardSheetRepository
import com.cheesecomer.rewardstamp.data.repository.RewardMilestoneRepository
import com.cheesecomer.rewardstamp.data.repository.RewardSheetRepository
import com.cheesecomer.rewardstamp.data.repository.RewardStampRepository
import com.cheesecomer.rewardstamp.data.rewardSheet
import com.cheesecomer.rewardstamp.model.StampType
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyBlocking
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class SheetDetailViewModelDoubleTapTest {
    @Test
    fun increment_ignoresSecondTapWhileStamping() =
        runTest {
            val gate = CompletableDeferred<Unit>()
            val sheet = rewardSheet(id = 1L, currentCount = 0, goalCount = 10)
            val rewardSheetRepository =
                mock<RewardSheetRepository> {
                    onBlocking { findById(any()) } doReturn sheet
                    onBlocking { increment(any()) } doSuspendableAnswer {
                        gate.await()
                        true
                    }
                }
            val rewardStampRepository =
                mock<RewardStampRepository> {
                    onBlocking { findBySheetId(any()) } doReturn emptyList()
                }
            val viewModel =
                SheetDetailViewModel(
                    rewardSheetRepository = rewardSheetRepository,
                    completedRewardSheetRepository = mock<CompletedRewardSheetRepository>(),
                    rewardStampRepository = rewardStampRepository,
                    rewardMilestoneRepository = mock<RewardMilestoneRepository>(),
                )
            viewModel.load(sheet.id)
            shadowOf(Looper.getMainLooper()).idle()

            viewModel.increment(sheet.id, StampType.Dog)
            viewModel.increment(sheet.id, StampType.Cat)
            gate.complete(Unit)
            shadowOf(Looper.getMainLooper()).idle()

            verifyBlocking(rewardSheetRepository, times(1)) { increment(any()) }
            verifyBlocking(rewardStampRepository, times(1)) { save(any()) }
        }
}
