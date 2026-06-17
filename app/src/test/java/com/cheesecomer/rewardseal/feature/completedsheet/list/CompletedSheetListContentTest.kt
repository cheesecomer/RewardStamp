package com.cheesecomer.rewardseal.feature.completedsheet.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardseal.data.completedRewardSheet
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class CompletedSheetListContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysCompletedSheets() {
        composeTestRule.setContent {
            RewardSealTheme {
                CompletedSheetListContent(
                    sheets =
                        listOf(
                            completedRewardSheet(
                                id = 1L,
                                title = "はみがき",
                                goalCount = 10,
                                consumedAt = null,
                            ),
                        ),
                )
            }
        }

        composeTestRule
            .onNodeWithText("これまでのがんばり")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("達成したごほうび：1こ")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("はみがき を 10回 がんばりました！")
            .assertIsDisplayed()
    }

    @Test
    fun displaysConsumedLabelWhenRewardIsConsumed() {
        composeTestRule.setContent {
            RewardSealTheme {
                CompletedSheetListContent(
                    sheets =
                        listOf(
                            completedRewardSheet(
                                id = 1L,
                                title = "はみがき",
                                goalCount = 10,
                                consumedAt = LocalDateTime.parse("2026-06-16T12:00:00"),
                            ),
                        ),
                )
            }
        }

        composeTestRule
            .onNodeWithText("交換済み")
            .assertIsDisplayed()
    }

    @Test
    fun displaysSheetCount() {
        composeTestRule.setContent {
            RewardSealTheme {
                CompletedSheetListContent(
                    sheets =
                        listOf(
                            completedRewardSheet(id = 1L),
                            completedRewardSheet(id = 2L),
                            completedRewardSheet(id = 3L),
                        ),
                )
            }
        }

        composeTestRule
            .onNodeWithText("達成したごほうび：3こ")
            .assertIsDisplayed()
    }

    @Test
    fun clickReward_callsCallback() {
        var clickedRewardId: Long? = null

        composeTestRule.setContent {
            RewardSealTheme {
                CompletedSheetListContent(
                    sheets =
                        listOf(
                            completedRewardSheet(
                                id = 123L,
                                title = "はみがき",
                                goalCount = 10,
                            ),
                        ),
                    onRewardClick = { rewardId ->
                        clickedRewardId = rewardId
                    },
                )
            }
        }

        composeTestRule
            .onNodeWithText("はみがき を 10回 がんばりました！")
            .performClick()

        assertThat(clickedRewardId).isEqualTo(123L)
    }
}
