package com.cheesecomer.rewardseal.feature.sheet.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardseal.data.rewardSheet
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SheetListContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun displaysInitialEmptyMessage() {
        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets = emptyList(),
                    completedSheetCount = 0,
                    onSheetClick = {},
                    onCreateSheetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("まだシートがありません").assertIsDisplayed()
        composeTestRule.onNodeWithText("「＋」から、がんばることを作ってみましょう").assertIsDisplayed()
    }

    @Test
    fun displaysEmptyMessageWhenCompletedSheetsExist() {
        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets = emptyList(),
                    completedSheetCount = 1,
                    onSheetClick = {},
                    onCreateSheetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("もうシートがありません").assertIsDisplayed()
    }

    @Test
    fun displaysSheets() {
        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets =
                        listOf(
                            rewardSheet(
                                id = 1L,
                                title = "はみがき",
                                currentCount = 3,
                                goalCount = 10,
                            ),
                        ),
                    completedSheetCount = 0,
                    onSheetClick = {},
                    onCreateSheetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("ごほうびスタンプ").assertIsDisplayed()
        composeTestRule.onNodeWithText("はみがき").assertIsDisplayed()
        composeTestRule.onNodeWithText("3 / 10").assertIsDisplayed()
    }

    @Test
    fun clickSheet_callsCallback() {
        var clickedSheetId: Long? = null

        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets = listOf(rewardSheet(id = 123L, title = "はみがき")),
                    completedSheetCount = 0,
                    onSheetClick = { clickedSheetId = it },
                    onCreateSheetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("はみがき").performClick()

        assertThat(clickedSheetId).isEqualTo(123L)
    }

    @Test
    fun clickFloatingActionButton_callsCallback() {
        var clicked = false

        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets = emptyList(),
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
