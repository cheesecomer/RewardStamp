package com.cheesecomer.rewardstamp.feature.completedsheet.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.cheesecomer.rewardstamp.data.completedRewardSheet
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CompletedSheetDetailContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysNotFoundMessageWhenSheetIsNull() {
        composeTestRule.setContent {
            RewardStampTheme {
                CompletedSheetDetailContent(
                    sheet = null,
                    stamps = emptyList(),
                )
            }
        }

        composeTestRule
            .onNodeWithText("見つかりません")
            .assertIsDisplayed()
    }

    @Test
    fun displaysCompletedReward() {
        composeTestRule.setContent {
            RewardStampTheme {
                CompletedSheetDetailContent(
                    sheet =
                        completedRewardSheet(
                            title = "はみがき",
                            goalCount = 15,
                            consumedAt = null,
                        ),
                    stamps = emptyList(),
                )
            }
        }

        composeTestRule
            .onNodeWithText("がんばったきろく")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("CompletedSheetDetailScreen.TitleLabel.Text")
            .assertTextEquals("はみがき")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("CompletedSheetDetailScreen.GoalCountLabel.Text")
            .assertTextEquals("15")
            .assertIsDisplayed()
    }
}
