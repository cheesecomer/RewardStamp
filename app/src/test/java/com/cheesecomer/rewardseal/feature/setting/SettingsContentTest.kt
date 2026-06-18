package com.cheesecomer.rewardseal.feature.setting

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
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
            RewardSealTheme {
                SettingsContent()
            }
        }

        composeTestRule
            .onNodeWithText("なにもないよ")
            .assertIsDisplayed()
    }
}
