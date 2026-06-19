package com.cheesecomer.rewardseal.feature.sheet.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardseal.data.rewardSheet
import com.cheesecomer.rewardseal.data.rewardStamp
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SheetListContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysInitialEmptyMessage() {
        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets = emptyList(),
                    latestStamps = emptyMap(),
                    completedSheetCount = 0,
                    onSheetClick = {},
                    onCreateSheetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("SheetListScreen.EmptyList").assertIsDisplayed()
    }

    @Test
    fun displaysEmptyMessageWhenCompletedSheetsExist() {
        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets = emptyList(),
                    latestStamps = emptyMap(),
                    completedSheetCount = 1,
                    onSheetClick = {},
                    onCreateSheetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("SheetListScreen.EmptyList").assertIsDisplayed()
    }

    @Test
    fun displaysSheets() {
        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets =
                        listOf(
                            rewardSheet(
                                id = 100L,
                                title = "はみがき",
                                currentCount = 3,
                                goalCount = 10,
                            ),
                        ),
                    latestStamps =
                        mapOf(
                            100L to rewardStamp(),
                        ),
                    completedSheetCount = 0,
                    onSheetClick = {},
                    onCreateSheetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("SheetListScreen.SheetCard.${100L}").assertIsDisplayed()
    }

    @Test
    fun clickSheet_callsCallback() {
        var clickedSheetId: Long? = null

        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets = listOf(rewardSheet(id = 123L, title = "はみがき")),
                    latestStamps =
                        mapOf(
                            123L to rewardStamp(),
                        ),
                    completedSheetCount = 0,
                    onSheetClick = { clickedSheetId = it },
                    onCreateSheetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithTag("SheetListScreen.SheetCard.${123L}").performClick()

        assertThat(clickedSheetId).isEqualTo(123L)
    }

    @Test
    fun clickFloatingActionButton_callsCallback() {
        var clicked = false

        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets = emptyList(),
                    latestStamps = emptyMap(),
                    completedSheetCount = 0,
                    onSheetClick = {},
                    onCreateSheetClick = { clicked = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("シートを作る").performClick()

        assertThat(clicked).isTrue()
    }
}
