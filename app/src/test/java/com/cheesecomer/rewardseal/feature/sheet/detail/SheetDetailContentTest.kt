package com.cheesecomer.rewardseal.feature.sheet.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardseal.data.rewardSheet
import com.cheesecomer.rewardseal.model.StampType
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SheetDetailContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysNotFoundMessageWhenSheetIsNull() {
        composeTestRule.setContent {
            SheetDetailContent(
                sheet = null,
                stamps = emptyList(),
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
            )
        }

        composeTestRule
            .onNodeWithText("見つかりません")
            .assertIsDisplayed()
    }

    @Test
    fun displaysProgressSheet() {
        composeTestRule.setContent {
            SheetDetailContent(
                sheet =
                    rewardSheet(
                        id = 1L,
                        title = "はみがき",
                        currentCount = 3,
                        goalCount = 10,
                    ),
                stamps = emptyList(),
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
            )
        }

        composeTestRule
            .onNodeWithTag("CongratulationDialog")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("はみがき")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("スタンプを押す")
            .assertIsDisplayed()
    }

    @Test
    fun displaysStampButtonWhenSheetIsCompleted() {
        composeTestRule.setContent {
            SheetDetailContent(
                sheet =
                    rewardSheet(
                        id = 1L,
                        title = "はみがき",
                        currentCount = 10,
                        goalCount = 10,
                    ),
                stamps = emptyList(),
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
            )
        }

        composeTestRule
            .onNodeWithText("スタンプを押す")
            .assertIsDisplayed()
    }

    @Test
    fun displaysCongratulationDialog() {
        composeTestRule.setContent {
            SheetDetailContent(
                sheet =
                    rewardSheet(
                        id = 1L,
                        title = "はみがき",
                        currentCount = 10,
                        goalCount = 10,
                    ),
                stamps = emptyList(),
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
            )
        }

        composeTestRule
            .onNodeWithTag("CongratulationDialog")
            .assertExists()
    }

    @Test
    fun clickBack_callsCallback() {
        var clicked = false

        composeTestRule.setContent {
            SheetDetailContent(
                sheet = rewardSheet(id = 1L, title = "はみがき"),
                stamps = emptyList(),
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
                onBackClick = {
                    clicked = true
                },
            )
        }

        composeTestRule
            .onNodeWithContentDescription("戻る")
            .performClick()

        assertThat(clicked).isTrue()
    }

    @Test
    fun clickEdit_callsCallback() {
        var clicked = false

        composeTestRule.setContent {
            SheetDetailContent(
                sheet = rewardSheet(id = 1L, title = "はみがき"),
                stamps = emptyList(),
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
                onEditClick = {
                    clicked = true
                },
            )
        }

        composeTestRule
            .onNodeWithTag("SheetDetailScreen.MenuButton")
            .performClick()

        composeTestRule
            .onNodeWithTag("SheetDetailScreen.Menu.EditButton")
            .performClick()

        assertThat(clicked).isTrue()
    }

    @Test
    fun clickRestart_callsCallback() {
        var clicked = false

        composeTestRule.setContent {
            SheetDetailContent(
                sheet =
                    rewardSheet(
                        id = 1L,
                        title = "はみがき",
                        currentCount = 10,
                        goalCount = 10,
                    ),
                stamps = emptyList(),
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
                onRestartClick = {
                    clicked = true
                },
            )
        }

        composeTestRule
            .onNodeWithTag("CongratulationDialog.SureButton")
            .performClick()

        assertThat(clicked).isTrue()
    }

    @Test
    fun clickRestartWithEdit_callsCallbackWithSheetId() {
        var onRestartWithEditClickCalled = false

        composeTestRule.setContent {
            SheetDetailContent(
                sheet =
                    rewardSheet(
                        id = 123L,
                        title = "はみがき",
                        currentCount = 10,
                        goalCount = 10,
                    ),
                stamps = emptyList(),
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
                onRestartWithEditClick = { onRestartWithEditClickCalled = true },
            )
        }

        composeTestRule
            .onNodeWithTag("CongratulationDialog.EditButton")
            .performClick()

        assertThat(onRestartWithEditClickCalled).isTrue()
    }

    @Test
    fun selectStamp_callsCallback() {
        var selectedStampType: StampType? = null

        composeTestRule.setContent {
            SheetDetailContent(
                sheet =
                    rewardSheet(
                        id = 1L,
                        title = "はみがき",
                        currentCount = 0,
                        goalCount = 10,
                    ),
                stamps = emptyList(),
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
                onStampTypeSelect = { stampType ->
                    selectedStampType = stampType
                },
            )
        }

        composeTestRule
            .onNodeWithText("スタンプを押す")
            .performClick()

        composeTestRule
            .onNodeWithTag(StampType.Hippopotamus.id)
            .performClick()

        assertThat(selectedStampType).isEqualTo(StampType.Hippopotamus)
    }

    @Test
    fun deleteSheet_callsCallback() {
        var deleted = false

        composeTestRule.setContent {
            SheetDetailContent(
                sheet = rewardSheet(id = 1L, title = "はみがき"),
                stamps = emptyList(),
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
                onDeleteClick = {
                    deleted = true
                },
            )
        }

        composeTestRule
            .onNodeWithTag("SheetDetailScreen.MenuButton")
            .performClick()

        composeTestRule
            .onNodeWithTag("SheetDetailScreen.Menu.DeleteButton")
            .performClick()

        composeTestRule
            .onNodeWithTag("DeleteSheetDialog.DeleteButton")
            .performClick()

        assertThat(deleted).isTrue()
    }
}
