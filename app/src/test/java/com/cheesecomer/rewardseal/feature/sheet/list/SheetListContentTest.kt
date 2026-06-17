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
                    exchangeableSheetCount = 0,
                    completedSheetCount = 0,
                    onSheetClick = {},
                    onUnreceivedRewardsClick = {},
                    onCompletedRewardsClick = {},
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
                    exchangeableSheetCount = 0,
                    completedSheetCount = 1,
                    onSheetClick = {},
                    onUnreceivedRewardsClick = {},
                    onCompletedRewardsClick = {},
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
                    exchangeableSheetCount = 0,
                    completedSheetCount = 0,
                    onSheetClick = {},
                    onUnreceivedRewardsClick = {},
                    onCompletedRewardsClick = {},
                    onCreateSheetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("ごほうびシール").assertIsDisplayed()
        composeTestRule.onNodeWithText("はみがき").assertIsDisplayed()
        composeTestRule.onNodeWithText("3 / 10").assertIsDisplayed()
    }

    @Test
    fun displaysNavigationCardsWhenCountsExist() {
        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets = emptyList(),
                    exchangeableSheetCount = 2,
                    completedSheetCount = 1,
                    onSheetClick = {},
                    onUnreceivedRewardsClick = {},
                    onCompletedRewardsClick = {},
                    onCreateSheetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("交換できるごほうびはあります：2件").assertIsDisplayed()
        composeTestRule.onNodeWithText("これまでのがんばりを見る").assertIsDisplayed()
    }

    @Test
    fun clickSheet_callsCallback() {
        var clickedSheetId: Long? = null

        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets = listOf(rewardSheet(id = 123L, title = "はみがき")),
                    exchangeableSheetCount = 0,
                    completedSheetCount = 0,
                    onSheetClick = { clickedSheetId = it },
                    onUnreceivedRewardsClick = {},
                    onCompletedRewardsClick = {},
                    onCreateSheetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("はみがき").performClick()

        assertThat(clickedSheetId).isEqualTo(123L)
    }

    @Test
    fun clickNavigationCards_callsCallbacks() {
        var clickedExchangeable = false
        var clickedCompleted = false

        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets = emptyList(),
                    exchangeableSheetCount = 1,
                    completedSheetCount = 1,
                    onSheetClick = {},
                    onUnreceivedRewardsClick = { clickedExchangeable = true },
                    onCompletedRewardsClick = { clickedCompleted = true },
                    onCreateSheetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("交換できるごほうびはあります：1件").performClick()
        composeTestRule.onNodeWithText("これまでのがんばりを見る").performClick()

        assertThat(clickedExchangeable).isTrue()
        assertThat(clickedCompleted).isTrue()
    }

    @Test
    fun clickFloatingActionButton_callsCallback() {
        var clicked = false

        composeTestRule.setContent {
            RewardSealTheme {
                SheetListContent(
                    sheets = emptyList(),
                    exchangeableSheetCount = 0,
                    completedSheetCount = 0,
                    onSheetClick = {},
                    onUnreceivedRewardsClick = {},
                    onCompletedRewardsClick = {},
                    onCreateSheetClick = { clicked = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("シートを作る").performClick()

        assertThat(clicked).isTrue()
    }
}
