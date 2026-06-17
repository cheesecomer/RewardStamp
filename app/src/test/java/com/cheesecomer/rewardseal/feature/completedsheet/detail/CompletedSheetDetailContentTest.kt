package com.cheesecomer.rewardseal.feature.completedsheet.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.cheesecomer.rewardseal.data.completedRewardSheet
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class CompletedSheetDetailContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysNotFoundMessageWhenSheetIsNull() {
        composeTestRule.setContent {
            RewardSealTheme {
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
            RewardSealTheme {
                CompletedSheetDetailContent(
                    sheet =
                        completedRewardSheet(
                            title = "はみがき",
                            goalCount = 10,
                            consumedAt = null,
                        ),
                    stamps = emptyList(),
                )
            }
        }

        composeTestRule
            .onNodeWithText("がんばった記録")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("はみがき を 10回 がんばりました！")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("交換済み")
            .assertDoesNotExist()
    }

    @Test
    fun displaysConsumedLabelWhenRewardIsConsumed() {
        composeTestRule.setContent {
            RewardSealTheme {
                CompletedSheetDetailContent(
                    sheet =
                        completedRewardSheet(
                            title = "はみがき",
                            goalCount = 10,
                            consumedAt = LocalDateTime.parse("2026-06-16T12:00:00"),
                        ),
                    stamps = emptyList(),
                )
            }
        }

        composeTestRule
            .onNodeWithText("交換済み")
            .assertIsDisplayed()
    }
}
