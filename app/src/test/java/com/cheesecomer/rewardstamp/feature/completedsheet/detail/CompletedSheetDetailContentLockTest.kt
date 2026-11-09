package com.cheesecomer.rewardstamp.feature.completedsheet.detail

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.data.completedRewardSheet
import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.model.LockTarget
import com.cheesecomer.rewardstamp.ui.component.dialog.DeleteSheetDialogTestTags
import com.cheesecomer.rewardstamp.ui.component.lock.AdultLockDialogTestTags
import com.cheesecomer.rewardstamp.ui.component.lock.AdultLockState
import com.cheesecomer.rewardstamp.ui.component.lock.rememberAdultLockState
import com.cheesecomer.rewardstamp.ui.component.lock.unlock
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.random.Random

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w411dp-h891dp")
class CompletedSheetDetailContentLockTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var adultLock: AdultLockState

    @Test
    fun delete_asksAdultBeforeConfirming() {
        composeTestRule.setContent {
            RewardStampTheme {
                adultLock =
                    rememberAdultLockState(AppSettings(adultLockEnabled = true, lockTargets = setOf(LockTarget.HistoryDelete)), Random(0))
                CompletedSheetDetailContent(
                    sheet = completedRewardSheet(),
                    stamps = emptyList(),
                    adultLock = adultLock,
                )
            }
        }

        composeTestRule.onNodeWithTag(CompletedSheetDetailTestTags.DELETE_BUTTON).performClick()

        composeTestRule.onNodeWithTag(DeleteSheetDialogTestTags.ROOT).assertDoesNotExist()
        composeTestRule.onNodeWithTag(AdultLockDialogTestTags.ROOT).assertExists()

        composeTestRule.unlock(adultLock)

        composeTestRule.onNodeWithTag(DeleteSheetDialogTestTags.ROOT).assertExists()
    }
}
