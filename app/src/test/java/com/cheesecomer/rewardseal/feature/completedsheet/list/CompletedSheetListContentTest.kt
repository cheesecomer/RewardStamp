package com.cheesecomer.rewardseal.feature.completedsheet.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
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
    fun displaysEmptyListWhenSheetsEmpty() {
        composeTestRule.setContent {
            RewardSealTheme {
                CompletedSheetListContent(
                    sheets =
                        emptyList(),
                )
            }
        }

        composeTestRule
            .onNodeWithTag("CompletedSheetListScreen.EmptyList")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("CompletedSheetListScreen.CompletedSheetGrid")
            .assertDoesNotExist()
    }

    @Test
    fun displaysCompletedSheetsWhenSheetsPresent() {
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
            .onNodeWithTag("CompletedSheetListScreen.EmptyList")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag("CompletedSheetListScreen.CompletedSheetGrid")
            .assertIsDisplayed()
    }

    @Test
    fun notDisplaysConsumedBadgeWhenNotRewardIsConsumed() {
        composeTestRule.setContent {
            RewardSealTheme {
                CompletedSheetListContent(
                    sheets =
                        listOf(
                            completedRewardSheet(
                                id = 123L,
                                title = "はみがき",
                                goalCount = 10,
                                consumedAt = null,
                            ),
                        ),
                )
            }
        }

        composeTestRule
            .onNodeWithTag("CompletedSheetListScreen.CompletedSheetGrid.123.RewardReceivedBadge")
            .isNotDisplayed()
    }

    @Test
    fun displaysConsumedBadgeWhenRewardIsConsumed() {
        composeTestRule.setContent {
            RewardSealTheme {
                CompletedSheetListContent(
                    sheets =
                        listOf(
                            completedRewardSheet(
                                id = 123L,
                                title = "はみがき",
                                goalCount = 10,
                                consumedAt = LocalDateTime.parse("2026-06-16T12:00:00"),
                            ),
                        ),
                )
            }
        }

        composeTestRule
            .onNodeWithTag("CompletedSheetListScreen.CompletedSheetGrid.123.RewardReceivedBadge")
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
                    onSheetClick = { rewardId ->
                        clickedRewardId = rewardId
                    },
                )
            }
        }

        composeTestRule
            .onNodeWithTag("CompletedSheetListScreen.CompletedSheetGrid.123")
            .performClick()

        assertThat(clickedRewardId).isEqualTo(123L)
    }
}
