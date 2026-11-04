package com.cheesecomer.rewardstamp.ui.component.dialog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.data.rewardMilestone
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
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
            RewardStampTheme {
                ChoiceRewardDialog(
                    milestones =
                        listOf(
                            rewardMilestone(
                                id = 1L,
                                requiredSheetCount = 1,
                                reward = "シール",
                            ),
                            rewardMilestone(
                                id = 2L,
                                requiredSheetCount = 3,
                                reward = "アイス",
                            ),
                        ),
                    onRewardSelect = {},
                    onDismissRequest = {},
                )
            }
        }

        composeTestRule.onNodeWithTag(ChoiceRewardDialogTestTags.reward(1)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(ChoiceRewardDialogTestTags.reward(2)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(DialogButtonsTestTags.CONFIRM_BUTTON).assertIsNotEnabled()
        composeTestRule.onNodeWithTag(DialogButtonsTestTags.DISMISS_BUTTON).assertIsEnabled()
    }

    @Test
    fun clickReward_enableConfirmButtonAndNotDismissesAndNotSelectsReward() {
        val milestone =
            rewardMilestone(
                id = 2L,
                requiredSheetCount = 3,
                reward = "アイス",
            )

        var dismissed = false
        var selectedMilestone: RewardMilestone? = null

        composeTestRule.setContent {
            RewardStampTheme {
                ChoiceRewardDialog(
                    milestones =
                        listOf(
                            rewardMilestone(
                                id = 1L,
                                requiredSheetCount = 1,
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

        composeTestRule.onNodeWithTag(ChoiceRewardDialogTestTags.reward(2)).performClick()

        assertThat(dismissed).isFalse()
        assertThat(selectedMilestone).isNull()
    }

    @Test
    fun clickConfirm_dismissesAndSelectsReward() {
        val milestone =
            rewardMilestone(
                id = 2L,
                requiredSheetCount = 3,
                reward = "アイス",
            )

        var dismissed = false
        var selectedMilestone: RewardMilestone? = null

        composeTestRule.setContent {
            RewardStampTheme {
                ChoiceRewardDialog(
                    milestones =
                        listOf(
                            rewardMilestone(
                                id = 1L,
                                requiredSheetCount = 1,
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

        composeTestRule.onNodeWithTag(ChoiceRewardDialogTestTags.reward(2)).performClick()
        composeTestRule.onNodeWithTag(DialogButtonsTestTags.CONFIRM_BUTTON).performClick()

        assertThat(dismissed).isTrue()
        assertThat(selectedMilestone).isEqualTo(milestone)
    }

    @Test
    fun clickCancel_dismissesDialog() {
        var dismissed = false

        composeTestRule.setContent {
            RewardStampTheme {
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

        composeTestRule.onNodeWithTag(DialogButtonsTestTags.DISMISS_BUTTON).performClick()

        assertThat(dismissed).isTrue()
    }
}
