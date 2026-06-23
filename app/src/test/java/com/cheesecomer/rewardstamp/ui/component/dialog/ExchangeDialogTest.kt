package com.cheesecomer.rewardstamp.ui.component.dialog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.data.rewardMilestone
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExchangeDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysDialog() {
        composeTestRule.setContent {
            RewardStampTheme {
                ExchangeDialog(
                    milestone =
                        rewardMilestone(
                            reward = "アイス",
                        ),
                    onRewardSelect = {},
                    onDismissRequest = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("ExchangeDialog.Background").assertIsDisplayed()
        composeTestRule.onNodeWithTag("DialogButtons.ConfirmButton").assertIsEnabled().assertIsDisplayed()
        composeTestRule.onNodeWithTag("DialogButtons.DismissButton").assertIsDisplayed()
    }

    @Test
    fun clickConfirm_dismissesAndSelectsReward() {
        val milestone = rewardMilestone(reward = "アイス")
        var dismissed = false
        var selectedMilestone = null as Any?

        composeTestRule.setContent {
            RewardStampTheme {
                ExchangeDialog(
                    milestone = milestone,
                    onRewardSelect = {
                        selectedMilestone = milestone
                    },
                    onDismissRequest = {
                        dismissed = true
                    },
                )
            }
        }

        composeTestRule.onNodeWithTag("DialogButtons.ConfirmButton").performClick()

        assertThat(dismissed).isTrue()
        assertThat(selectedMilestone).isEqualTo(milestone)
    }

    @Test
    fun clickCancel_dismissesDialog() {
        var dismissed = false

        composeTestRule.setContent {
            RewardStampTheme {
                ExchangeDialog(
                    milestone = rewardMilestone(reward = "アイス"),
                    onRewardSelect = {},
                    onDismissRequest = {
                        dismissed = true
                    },
                )
            }
        }

        composeTestRule.onNodeWithTag("DialogButtons.DismissButton").performClick()

        assertThat(dismissed).isTrue()
    }
}
