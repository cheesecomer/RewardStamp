package com.cheesecomer.rewardseal.ui.component.dialog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardseal.data.rewardMilestone
import com.cheesecomer.rewardseal.model.RewardMilestone
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ChoiceRewardDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysDialog() {
        composeTestRule.setContent {
            RewardSealTheme {
                ChoiceRewardDialog(
                    milestones =
                        listOf(
                            rewardMilestone(
                                requiredCompletions = 1,
                                reward = "シール",
                            ),
                            rewardMilestone(
                                requiredCompletions = 3,
                                reward = "アイス",
                            ),
                        ),
                    onRewardSelect = {},
                    onDismissRequest = {},
                )
            }
        }

        composeTestRule.onNodeWithText("ごほうびを選んでね").assertIsDisplayed()
        composeTestRule.onNodeWithText("シール（1枚）").assertIsDisplayed()
        composeTestRule.onNodeWithText("アイス（3枚）").assertIsDisplayed()
        composeTestRule.onNodeWithText("キャンセル").assertIsDisplayed()
    }

    @Test
    fun clickReward_dismissesAndSelectsReward() {
        val milestone =
            rewardMilestone(
                requiredCompletions = 3,
                reward = "アイス",
            )

        var dismissed = false
        var selectedMilestone: RewardMilestone? = null

        composeTestRule.setContent {
            RewardSealTheme {
                ChoiceRewardDialog(
                    milestones =
                        listOf(
                            rewardMilestone(
                                requiredCompletions = 1,
                                reward = "シール",
                            ),
                            milestone,
                        ),
                    onRewardSelect = {
                        selectedMilestone = it
                    },
                    onDismissRequest = {
                        dismissed = true
                    },
                )
            }
        }

        composeTestRule.onNodeWithText("アイス（3枚）").performClick()

        assertThat(dismissed).isTrue()
        assertThat(selectedMilestone).isEqualTo(milestone)
    }

    @Test
    fun clickCancel_dismissesDialog() {
        var dismissed = false

        composeTestRule.setContent {
            RewardSealTheme {
                ChoiceRewardDialog(
                    milestones =
                        listOf(
                            rewardMilestone(
                                reward = "アイス",
                            ),
                        ),
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
