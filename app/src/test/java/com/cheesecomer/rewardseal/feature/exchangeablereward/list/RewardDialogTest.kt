package com.cheesecomer.rewardseal.feature.exchangeablereward.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.cheesecomer.rewardseal.data.exchangeableSheet
import com.cheesecomer.rewardseal.data.rewardMilestone
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RewardDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysExchangeDialogWhenMilestoneIsSingle() {
        composeTestRule.setContent {
            RewardSealTheme {
                RewardDialog(
                    sheet =
                        exchangeableSheet(
                            exchangeableMilestones =
                                listOf(
                                    rewardMilestone(
                                        requiredCompletions = 1,
                                        reward = "アイス",
                                    ),
                                ),
                        ),
                )
            }
        }
        composeTestRule
            .onNodeWithTag("ChoiceRewardDialog")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag("ExchangeDialog")
            .assertIsDisplayed()
    }

    @Test
    fun displaysChoiceRewardDialogWhenMilestonesAreMultiple() {
        composeTestRule.setContent {
            RewardSealTheme {
                RewardDialog(
                    sheet =
                        exchangeableSheet(
                            exchangeableMilestones =
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
                        ),
                )
            }
        }
        composeTestRule
            .onNodeWithTag("ChoiceRewardDialog")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("ExchangeDialog")
            .assertDoesNotExist()
    }
}
