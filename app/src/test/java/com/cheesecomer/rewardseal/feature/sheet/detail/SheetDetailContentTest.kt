package com.cheesecomer.rewardseal.feature.sheet.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
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
                sheetId = 1L,
                sheet = null,
                stamps = emptyList(),
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
                sheetId = 1L,
                sheet =
                    rewardSheet(
                        id = 1L,
                        title = "はみがき",
                        currentCount = 3,
                        goalCount = 10,
                    ),
                stamps = emptyList(),
            )
        }

        composeTestRule
            .onNodeWithText("はみがき")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("3 / 10")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("スタンプを押す")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("編集")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("削除")
            .assertIsDisplayed()
    }

    @Test
    fun disablesStampButtonWhenSheetIsCompleted() {
        composeTestRule.setContent {
            SheetDetailContent(
                sheetId = 1L,
                sheet =
                    rewardSheet(
                        id = 1L,
                        title = "はみがき",
                        currentCount = 10,
                        goalCount = 10,
                    ),
                stamps = emptyList(),
            )
        }

        composeTestRule
            .onNodeWithText("スタンプを押す")
            .assertIsNotDisplayed()
    }

    @Test
    fun displaysCompletedSheetActions() {
        composeTestRule.setContent {
            SheetDetailContent(
                sheetId = 1L,
                sheet =
                    rewardSheet(
                        id = 1L,
                        title = "はみがき",
                        currentCount = 10,
                        goalCount = 10,
                    ),
                stamps = emptyList(),
            )
        }

        composeTestRule
            .onNodeWithText("🎉 シートが満タンになりました！")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("はみがき を 10回 がんばってシートを満タンにしました。")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("もっとがんばる")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("ごほうびや回数を変えてがんばる")
            .assertIsDisplayed()
    }

    @Test
    fun clickBack_callsCallback() {
        var clicked = false

        composeTestRule.setContent {
            SheetDetailContent(
                sheetId = 1L,
                sheet = rewardSheet(id = 1L, title = "はみがき"),
                stamps = emptyList(),
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
                sheetId = 1L,
                sheet = rewardSheet(id = 1L, title = "はみがき"),
                stamps = emptyList(),
                onEditClick = {
                    clicked = true
                },
            )
        }

        composeTestRule
            .onNodeWithText("編集")
            .performClick()

        assertThat(clicked).isTrue()
    }

    @Test
    fun clickRestart_callsCallback() {
        var clicked = false

        composeTestRule.setContent {
            SheetDetailContent(
                sheetId = 1L,
                sheet =
                    rewardSheet(
                        id = 1L,
                        title = "はみがき",
                        currentCount = 10,
                        goalCount = 10,
                    ),
                stamps = emptyList(),
                onRestartClick = {
                    clicked = true
                },
            )
        }

        composeTestRule
            .onNodeWithText("もっとがんばる")
            .performClick()

        assertThat(clicked).isTrue()
    }

    @Test
    fun clickRestartWithEdit_callsCallbackWithSheetId() {
        var clickedSheetId: Long? = null

        composeTestRule.setContent {
            SheetDetailContent(
                sheetId = 123L,
                sheet =
                    rewardSheet(
                        id = 123L,
                        title = "はみがき",
                        currentCount = 10,
                        goalCount = 10,
                    ),
                stamps = emptyList(),
                onRestartWithEditClick = { sheetId ->
                    clickedSheetId = sheetId
                },
            )
        }

        composeTestRule
            .onNodeWithText("ごほうびや回数を変えてがんばる")
            .performClick()

        assertThat(clickedSheetId).isEqualTo(123L)
    }

    @Test
    fun selectStamp_callsCallback() {
        var selectedStampType: StampType? = null

        composeTestRule.setContent {
            SheetDetailContent(
                sheetId = 1L,
                sheet =
                    rewardSheet(
                        id = 1L,
                        title = "はみがき",
                        currentCount = 0,
                        goalCount = 10,
                    ),
                stamps = emptyList(),
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
                sheetId = 1L,
                sheet = rewardSheet(id = 1L, title = "はみがき"),
                stamps = emptyList(),
                onDeleteClick = {
                    deleted = true
                },
            )
        }

        composeTestRule
            .onNodeWithText("削除")
            .performClick()

        composeTestRule
            .onNodeWithTag("DeleteSheetDialog.DeleteButton")
            .performClick()

        assertThat(deleted).isTrue()
    }
}
