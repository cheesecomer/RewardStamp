package com.cheesecomer.rewardstamp.feature.exchangeablesheet.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.cheesecomer.rewardstamp.data.exchangeableSheet
import com.cheesecomer.rewardstamp.data.rewardMilestone
import com.cheesecomer.rewardstamp.ui.component.dialog.ChoiceRewardDialogTestTags
import com.cheesecomer.rewardstamp.ui.component.dialog.ExchangeDialogTestTags
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
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
            RewardStampTheme {
                RewardDialog(
                    sheet =
                        exchangeableSheet(
                            exchangeableMilestones =
                                listOf(
                                    rewardMilestone(
                                        requiredSheetCount = 1,
                                        reward = "アイス",
                                    ),
                                ),
                        ),
                )
            }
        }
        composeTestRule
            .onNodeWithTag(ChoiceRewardDialogTestTags.ROOT)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag(ExchangeDialogTestTags.ROOT)
            .assertIsDisplayed()
    }

    @Test
    fun displaysChoiceRewardDialogWhenMilestonesAreMultiple() {
        composeTestRule.setContent {
            RewardStampTheme {
                RewardDialog(
                    sheet =
                        exchangeableSheet(
                            exchangeableMilestones =
                                listOf(
                                    rewardMilestone(
                                        requiredSheetCount = 1,
                                        reward = "シール",
                                    ),
                                    rewardMilestone(
                                        requiredSheetCount = 3,
                                        reward = "アイス",
                                    ),
                                ),
                        ),
                )
            }
        }
        composeTestRule
            .onNodeWithTag(ChoiceRewardDialogTestTags.ROOT)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(ExchangeDialogTestTags.ROOT)
            .assertDoesNotExist()
    }
}
