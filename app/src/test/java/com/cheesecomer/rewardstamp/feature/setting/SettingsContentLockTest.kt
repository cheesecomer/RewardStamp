package com.cheesecomer.rewardstamp.feature.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.model.AppSettings
import com.cheesecomer.rewardstamp.model.LockTarget
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
class SettingsContentLockTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var adultLock: AdultLockState

    // Behaves like the saved settings, so that the screen follows each change
    private fun setContent(initial: AppSettings) {
        composeTestRule.setContent {
            var settings by remember { mutableStateOf(initial) }
            adultLock = rememberAdultLockState(settings, Random(0))
            RewardStampTheme {
                SettingsContent(
                    settings = settings,
                    versionName = "1.0.0",
                    onAdultLockChange = { settings = settings.copy(adultLockEnabled = it) },
                    onLockTargetChange = { target, locked ->
                        settings =
                            settings.copy(lockTargets = if (locked) settings.lockTargets + target else settings.lockTargets - target)
                    },
                    adultLock = adultLock,
                )
            }
        }
    }

    private fun assertAsking(asking: Boolean) {
        val node = composeTestRule.onNodeWithTag(AdultLockDialogTestTags.ROOT)
        if (asking) node.assertExists() else node.assertDoesNotExist()
    }

    @Test
    fun adultLockOff_hidesTargetsAndTurnsOnWithoutAsking() {
        setContent(AppSettings(adultLockEnabled = false))
        composeTestRule.onNodeWithTag(SettingsTestTags.lockTarget(LockTarget.Stamp)).assertDoesNotExist()

        composeTestRule.onNodeWithTag(SettingsTestTags.ADULT_LOCK_SWITCH).performClick()

        assertAsking(false)
        composeTestRule.onNodeWithTag(SettingsTestTags.ADULT_LOCK_SWITCH).assertIsOn()
        // Only stamping is chosen at first
        composeTestRule.onNodeWithTag(SettingsTestTags.lockTarget(LockTarget.Stamp)).assertIsOn()
        listOf(LockTarget.RewardExchange, LockTarget.SheetEdit, LockTarget.HistoryDelete).forEach { target ->
            composeTestRule.onNodeWithTag(SettingsTestTags.lockTarget(target)).assertIsOff()
        }
        composeTestRule.onNodeWithTag(SettingsTestTags.lockTarget(LockTarget.LockSettings)).assertDoesNotExist()
    }

    @Test
    fun adultLockOn_asksOnceBeforeChangingLockSettings() {
        setContent(AppSettings(adultLockEnabled = true))

        val stamp = composeTestRule.onNodeWithTag(SettingsTestTags.lockTarget(LockTarget.Stamp))
        stamp.performClick()
        assertAsking(true)
        stamp.assertIsOn()

        composeTestRule.unlock(adultLock)
        stamp.assertIsOff()

        // The unlock lasts while the screen is open
        val exchange = composeTestRule.onNodeWithTag(SettingsTestTags.lockTarget(LockTarget.RewardExchange))
        exchange.performClick()
        assertAsking(false)
        exchange.assertIsOn()

        composeTestRule.onNodeWithTag(SettingsTestTags.ADULT_LOCK_SWITCH).performClick()
        assertAsking(false)
        composeTestRule.onNodeWithTag(SettingsTestTags.ADULT_LOCK_SWITCH).assertIsOff()
    }
}
