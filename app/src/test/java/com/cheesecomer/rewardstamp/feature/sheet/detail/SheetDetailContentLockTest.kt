package com.cheesecomer.rewardstamp.feature.sheet.detail

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.data.rewardSheet
import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.model.StampType
import com.cheesecomer.rewardstamp.ui.component.dialog.DeleteSheetDialogTestTags
import com.cheesecomer.rewardstamp.ui.component.lock.AdultLockDialogTestTags
import com.cheesecomer.rewardstamp.ui.component.lock.AdultLockState
import com.cheesecomer.rewardstamp.ui.component.lock.rememberAdultLockState
import com.cheesecomer.rewardstamp.ui.component.lock.unlock
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.random.Random

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w411dp-h891dp")
class SheetDetailContentLockTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var adultLock: AdultLockState
    private val selectedStamps = mutableListOf<StampType>()
    private var editClicked = false
    private var restartWithEditClicked = false

    private fun setContent(
        settings: AppSettings = AppSettings(adultLockEnabled = true),
        currentCount: Int = 0,
    ) {
        composeTestRule.setContent {
            adultLock = rememberAdultLockState(settings, Random(0))
            SheetDetailContent(
                sheet = rewardSheet(id = 1L, title = "はみがき", currentCount = currentCount, goalCount = 10),
                stamps = emptyList(),
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
                adultLock = adultLock,
                onEditClick = { editClicked = true },
                onStampTypeSelect = { selectedStamps += it },
                onRestartWithEditClick = { restartWithEditClicked = true },
            )
        }
    }

    private fun clickStampButton() {
        composeTestRule.onNodeWithText("スタンプを押す").performClick()
    }

    private fun assertAsking(asking: Boolean) {
        val node = composeTestRule.onNodeWithTag(AdultLockDialogTestTags.ROOT)
        if (asking) node.assertExists() else node.assertDoesNotExist()
    }

    @Test
    fun stamp_staysUnlockedUntilStamped() {
        setContent()

        clickStampButton()
        assertAsking(true)
        composeTestRule.unlock(adultLock)

        // Closing the picker to look at the sheet again does not lock it
        composeTestRule.onNodeWithText("キャンセル").performClick()
        clickStampButton()
        assertAsking(false)

        composeTestRule.onNodeWithTag(StampPickerTestTags.stamp(StampType.Cat)).performClick()
        assertThat(selectedStamps).containsExactly(StampType.Cat)

        // After stamping, the next stamp asks again
        clickStampButton()
        assertAsking(true)
    }

    @Test
    fun stamp_isLeftToChildrenWhenStampLockIsOff() {
        setContent(AppSettings(adultLockEnabled = true, stampLockEnabled = false))

        clickStampButton()

        assertAsking(false)
        composeTestRule.onNodeWithTag(StampPickerTestTags.stamp(StampType.Cat)).assertExists()
    }

    @Test
    fun edit_asksAdult() {
        setContent()

        composeTestRule.onNodeWithTag(SheetDetailTestTags.MENU_BUTTON).performClick()
        composeTestRule.onNodeWithTag(SheetDetailTestTags.MENU_EDIT_BUTTON).performClick()
        assertThat(editClicked).isFalse()

        composeTestRule.unlock(adultLock)

        assertThat(editClicked).isTrue()
    }

    @Test
    fun delete_asksAdultBeforeConfirming() {
        setContent()

        composeTestRule.onNodeWithTag(SheetDetailTestTags.MENU_BUTTON).performClick()
        composeTestRule.onNodeWithTag(SheetDetailTestTags.MENU_DELETE_BUTTON).performClick()
        composeTestRule.onNodeWithTag(DeleteSheetDialogTestTags.ROOT).assertDoesNotExist()

        composeTestRule.unlock(adultLock)

        composeTestRule.onNodeWithTag(DeleteSheetDialogTestTags.ROOT).assertExists()
    }

    @Test
    fun editRewardAfterGoal_asksAdult() {
        setContent(currentCount = 10)

        composeTestRule.onNodeWithTag(CongratulationDialogTestTags.EDIT_BUTTON).performClick()
        assertThat(restartWithEditClicked).isFalse()

        composeTestRule.unlock(adultLock)

        assertThat(restartWithEditClicked).isTrue()
    }
}
