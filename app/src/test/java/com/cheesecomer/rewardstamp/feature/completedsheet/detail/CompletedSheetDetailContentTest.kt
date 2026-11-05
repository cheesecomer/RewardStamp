package com.cheesecomer.rewardstamp.feature.completedsheet.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.data.completedRewardSheet
import com.cheesecomer.rewardstamp.ui.component.dialog.DeleteSheetDialogTestTags
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CompletedSheetDetailContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysNotFoundMessageWhenSheetIsNull() {
        composeTestRule.setContent {
            RewardStampTheme {
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
            RewardStampTheme {
                CompletedSheetDetailContent(
                    sheet =
                        completedRewardSheet(
                            title = "はみがき",
                            goalCount = 15,
                            consumedAt = null,
                        ),
                    stamps = emptyList(),
                )
            }
        }

        composeTestRule
            .onNodeWithText("がんばったきろく")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag(CompletedSheetDetailTestTags.TITLE_LABEL_TEXT)
            .assertTextEquals("はみがき")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag(CompletedSheetDetailTestTags.GOAL_COUNT_LABEL_TEXT)
            .assertTextEquals("15")
            .assertIsDisplayed()
    }

    @Test
    fun callsOnDeleteClickWhenDeleteIsConfirmed() {
        var deleted = false
        composeTestRule.setContent {
            RewardStampTheme {
                CompletedSheetDetailContent(
                    sheet = completedRewardSheet(),
                    stamps = emptyList(),
                    onDeleteClick = {
                        deleted = true
                    },
                )
            }
        }

        composeTestRule
            .onNodeWithTag(CompletedSheetDetailTestTags.DELETE_BUTTON)
            .performClick()
        composeTestRule
            .onNodeWithText("きろくを削除しますか？")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag(DeleteSheetDialogTestTags.DELETE_BUTTON)
            .performClick()

        assertThat(deleted).isTrue()
        composeTestRule
            .onNodeWithTag(DeleteSheetDialogTestTags.ROOT)
            .assertDoesNotExist()
    }

    @Test
    fun doesNotCallOnDeleteClickWhenDeleteIsCanceled() {
        var deleted = false
        composeTestRule.setContent {
            RewardStampTheme {
                CompletedSheetDetailContent(
                    sheet = completedRewardSheet(),
                    stamps = emptyList(),
                    onDeleteClick = {
                        deleted = true
                    },
                )
            }
        }

        composeTestRule
            .onNodeWithTag(CompletedSheetDetailTestTags.DELETE_BUTTON)
            .performClick()
        composeTestRule
            .onNodeWithText("キャンセル")
            .performClick()

        assertThat(deleted).isFalse()
        composeTestRule
            .onNodeWithTag(DeleteSheetDialogTestTags.ROOT)
            .assertDoesNotExist()
    }
}
