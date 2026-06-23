package com.cheesecomer.rewardstamp.feature.setting

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysEmptyMessage() {
        composeTestRule.setContent {
            RewardStampTheme {
                SettingsContent()
            }
        }

        composeTestRule
            .onNodeWithText("なにもないよ")
            .assertIsDisplayed()
    }
}
