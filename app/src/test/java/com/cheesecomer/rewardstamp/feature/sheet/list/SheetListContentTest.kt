package com.cheesecomer.rewardstamp.feature.sheet.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.data.rewardSheet
import com.cheesecomer.rewardstamp.data.rewardStamp
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
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
            RewardStampTheme {
                SheetListContent(
                    sheets = emptyList(),
                    latestStamps = emptyMap(),
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
            RewardStampTheme {
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
            RewardStampTheme {
                SheetListContent(
                    sheets = listOf(rewardSheet(id = 123L, title = "はみがき")),
                    latestStamps =
                        mapOf(
                            123L to rewardStamp(),
                        ),
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
            RewardStampTheme {
                SheetListContent(
                    sheets = emptyList(),
                    latestStamps = emptyMap(),
                    onSheetClick = {},
                    onCreateSheetClick = { clicked = true },
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("シートを作る").performClick()

        assertThat(clicked).isTrue()
    }
}
