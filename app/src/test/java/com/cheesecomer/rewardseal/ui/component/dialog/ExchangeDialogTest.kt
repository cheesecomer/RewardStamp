package com.cheesecomer.rewardseal.ui.component.dialog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardseal.data.rewardMilestone
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
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
            RewardSealTheme {
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

        composeTestRule.onNodeWithText("交換しますか？").assertIsDisplayed()
        composeTestRule.onNodeWithText("アイス と交換しますか？").assertIsDisplayed()
        composeTestRule.onNodeWithText("交換する").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
    }

    @Test
    fun clickConfirm_dismissesAndSelectsReward() {
        val milestone = rewardMilestone(reward = "アイス")
        var dismissed = false
        var selectedMilestone = null as Any?

        composeTestRule.setContent {
            RewardSealTheme {
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

        composeTestRule.onNodeWithText("交換する").performClick()

        assertThat(dismissed).isTrue()
        assertThat(selectedMilestone).isEqualTo(milestone)
    }

    @Test
    fun clickCancel_dismissesDialog() {
        var dismissed = false

        composeTestRule.setContent {
            RewardSealTheme {
                ExchangeDialog(
                    milestone = rewardMilestone(reward = "アイス"),
                    onRewardSelect = {},
                    onDismissRequest = {
                        dismissed = true
                    },
                )
            }
        }

        composeTestRule.onNodeWithText("キャンセル").performClick()

        assertThat(dismissed).isTrue()
    }
}
