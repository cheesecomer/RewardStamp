package com.cheesecomer.rewardstamp.feature.exchangeablesheet.list

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.data.exchangeableSheet
import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.model.LockTarget
import com.cheesecomer.rewardstamp.ui.component.dialog.ExchangeDialogTestTags
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
class ExchangeableSheetListContentLockTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var adultLock: AdultLockState
    private val sheet = exchangeableSheet(id = 7L)

    private fun setContent(settings: AppSettings) {
        composeTestRule.setContent {
            RewardStampTheme {
                adultLock = rememberAdultLockState(settings, Random(0))
                ExchangeableSheetListContent(
                    sheets = listOf(sheet),
                    adultLock = adultLock,
                )
            }
        }
    }

    @Test
    fun exchange_asksAdultBeforeChoosingReward() {
        setContent(AppSettings(adultLockEnabled = true, lockTargets = setOf(LockTarget.RewardExchange)))

        composeTestRule.onNodeWithTag(ExchangeableSheetCardTestTags.exchangeButton(7L)).performClick()

        composeTestRule.onNodeWithTag(ExchangeDialogTestTags.ROOT).assertDoesNotExist()
        composeTestRule.onNodeWithTag(AdultLockDialogTestTags.ROOT).assertExists()

        composeTestRule.unlock(adultLock)

        composeTestRule.onNodeWithTag(ExchangeDialogTestTags.ROOT).assertExists()
    }

    @Test
    fun exchange_doesNotAskWhenNotChosen() {
        setContent(AppSettings(adultLockEnabled = true, lockTargets = setOf(LockTarget.Stamp)))

        composeTestRule.onNodeWithTag(ExchangeableSheetCardTestTags.exchangeButton(7L)).performClick()

        composeTestRule.onNodeWithTag(ExchangeDialogTestTags.ROOT).assertExists()
    }
}
