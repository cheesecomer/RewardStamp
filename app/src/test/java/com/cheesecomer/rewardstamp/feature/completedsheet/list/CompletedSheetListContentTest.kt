package com.cheesecomer.rewardstamp.feature.completedsheet.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.data.completedRewardSheet
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
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
            RewardStampTheme {
                CompletedSheetListContent(
                    sheets =
                        emptyList(),
                )
            }
        }

        composeTestRule
            .onNodeWithTag(CompletedSheetListTestTags.EMPTY_LIST)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(CompletedSheetListTestTags.GRID)
            .assertDoesNotExist()
    }

    @Test
    fun displaysCompletedSheetsWhenSheetsPresent() {
        composeTestRule.setContent {
            RewardStampTheme {
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
            .onNodeWithTag(CompletedSheetListTestTags.EMPTY_LIST)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag(CompletedSheetListTestTags.GRID)
            .assertIsDisplayed()
    }

    @Test
    fun notDisplaysConsumedBadgeWhenNotRewardIsConsumed() {
        composeTestRule.setContent {
            RewardStampTheme {
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
            .onNodeWithTag(CompletedSheetListTestTags.rewardReceivedBadge(123))
            .isNotDisplayed()
    }

    @Test
    fun displaysConsumedBadgeWhenRewardIsConsumed() {
        composeTestRule.setContent {
            RewardStampTheme {
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
            .onNodeWithTag(CompletedSheetListTestTags.rewardReceivedBadge(123))
            .assertIsDisplayed()
    }

    @Test
    fun clickReward_callsCallback() {
        var clickedRewardId: Long? = null

        composeTestRule.setContent {
            RewardStampTheme {
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
            .onNodeWithTag(CompletedSheetListTestTags.gridItem(123))
            .performClick()

        assertThat(clickedRewardId).isEqualTo(123L)
    }
}
