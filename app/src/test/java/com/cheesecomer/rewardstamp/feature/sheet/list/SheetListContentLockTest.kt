package com.cheesecomer.rewardstamp.feature.sheet.list

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.model.LockTarget
import com.cheesecomer.rewardstamp.ui.component.lock.AdultLockDialogTestTags
import com.cheesecomer.rewardstamp.ui.component.lock.AdultLockState
import com.cheesecomer.rewardstamp.ui.component.lock.rememberAdultLockState
import com.cheesecomer.rewardstamp.ui.component.lock.unlock
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.random.Random

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w411dp-h891dp")
class SheetListContentLockTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var adultLock: AdultLockState
    private var createClicked = false

    private fun setContent(settings: AppSettings) {
        composeTestRule.setContent {
            RewardStampTheme {
                adultLock = rememberAdultLockState(settings, Random(0))
                SheetListContent(
                    sheets = emptyList(),
                    latestStamps = emptyMap(),
                    onSheetClick = {},
                    onCreateSheetClick = { createClicked = true },
                    adultLock = adultLock,
                )
            }
        }
    }

    @Test
    fun createSheet_asksAdultWhenLocked() {
        setContent(AppSettings(adultLockEnabled = true, lockTargets = setOf(LockTarget.SheetEdit)))

        composeTestRule.onNodeWithContentDescription("シートを作る").performClick()

        assertThat(createClicked).isFalse()
        composeTestRule.onNodeWithTag(AdultLockDialogTestTags.ROOT).assertExists()

        composeTestRule.unlock(adultLock)

        assertThat(createClicked).isTrue()
    }

    @Test
    fun createSheet_doesNotAskWhenNotChosen() {
        setContent(AppSettings(adultLockEnabled = true, lockTargets = setOf(LockTarget.Stamp)))

        composeTestRule.onNodeWithContentDescription("シートを作る").performClick()

        assertThat(createClicked).isTrue()
        composeTestRule.onNodeWithTag(AdultLockDialogTestTags.ROOT).assertDoesNotExist()
    }
}
