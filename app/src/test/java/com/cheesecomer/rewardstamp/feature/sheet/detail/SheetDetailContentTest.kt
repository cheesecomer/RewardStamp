package com.cheesecomer.rewardstamp.feature.sheet.detail

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.cheesecomer.rewardstamp.data.rewardSheet
import com.cheesecomer.rewardstamp.data.rewardStamp
import com.cheesecomer.rewardstamp.model.GoalStampType
import com.cheesecomer.rewardstamp.model.RewardStamp
import com.cheesecomer.rewardstamp.model.StampType
import com.cheesecomer.rewardstamp.ui.component.BoardLayoutMetrics
import com.cheesecomer.rewardstamp.ui.component.RewardBoardViewTestTags
import com.cheesecomer.rewardstamp.ui.component.boardPatterns
import com.cheesecomer.rewardstamp.ui.component.buildBoardPoints
import com.cheesecomer.rewardstamp.ui.component.dialog.DeleteSheetDialogTestTags
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
            .onNodeWithTag(CongratulationDialogTestTags.ROOT)
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
            .onNodeWithTag(CongratulationDialogTestTags.ROOT)
            .assertExists()
    }

    @Test
    fun goalStampPressed_celebratesBeforeCongratulationDialog() {
        composeTestRule.mainClock.autoAdvance = false
        setGoalReachedContent(goalStampType = GoalStampType.Bear1)

        composeTestRule
            .onNodeWithTag(CongratulationDialogTestTags.ROOT)
            .assertDoesNotExist()

        composeTestRule.mainClock.advanceTimeBy(1_000)
        composeTestRule
            .onNodeWithTag(GoalCelebrationTestTags.GOAL_STAMP, useUnmergedTree = true)
            .assertExists()

        composeTestRule.mainClock.advanceTimeBy(5_000)
        composeTestRule
            .onNodeWithTag(GoalCelebrationTestTags.ROOT)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag(CongratulationDialogTestTags.ROOT)
            .assertExists()
    }

    @Test
    fun goalCelebration_skipsToCongratulationDialogOnClick() {
        composeTestRule.mainClock.autoAdvance = false
        setGoalReachedContent(goalStampType = GoalStampType.Bear1)

        composeTestRule.mainClock.advanceTimeBy(1_000)
        composeTestRule
            .onNodeWithTag(GoalCelebrationTestTags.ROOT)
            .performClick()
        composeTestRule.mainClock.advanceTimeBy(100)

        composeTestRule
            .onNodeWithTag(CongratulationDialogTestTags.ROOT)
            .assertExists()
    }

    @Test
    fun goalCelebration_givesUpWhenGoalStampDoesNotArrive() {
        composeTestRule.mainClock.autoAdvance = false
        setGoalReachedContent(goalStampType = null)

        composeTestRule.mainClock.advanceTimeBy(1_000)
        composeTestRule
            .onNodeWithTag(CongratulationDialogTestTags.ROOT)
            .assertDoesNotExist()

        composeTestRule.mainClock.advanceTimeBy(5_000)
        composeTestRule
            .onNodeWithTag(CongratulationDialogTestTags.ROOT)
            .assertExists()
    }

    @Test
    fun stampPressedBeforeGoal_doesNotCelebrate() {
        composeTestRule.mainClock.autoAdvance = false
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
                stampedPosition = 2,
            )
        }

        composeTestRule
            .onNodeWithTag(GoalCelebrationTestTags.ROOT)
            .assertDoesNotExist()

        composeTestRule.mainClock.advanceTimeBy(1_000)
        composeTestRule
            .onNodeWithText("はみがき")
            .assertIsDisplayed()
    }

    private fun setGoalReachedContent(goalStampType: GoalStampType?) {
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
                goalStampType = goalStampType,
                stampedPosition = 9,
            )
        }
    }

    @Test
    fun displaysProgressSheetWithGoalStamp() {
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
                goalStampType = GoalStampType.Bear1,
            )
        }

        composeTestRule
            .onNodeWithText("はみがき")
            .assertIsDisplayed()
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
            .onNodeWithTag(SheetDetailTestTags.MENU_BUTTON)
            .performClick()

        composeTestRule
            .onNodeWithTag(SheetDetailTestTags.MENU_EDIT_BUTTON)
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
            .onNodeWithTag(CongratulationDialogTestTags.SURE_BUTTON)
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
            .onNodeWithTag(CongratulationDialogTestTags.EDIT_BUTTON)
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
            .onNodeWithTag(StampPickerTestTags.stamp(StampType.Hippopotamus))
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
            .onNodeWithTag(SheetDetailTestTags.MENU_BUTTON)
            .performClick()

        composeTestRule
            .onNodeWithTag(SheetDetailTestTags.MENU_DELETE_BUTTON)
            .performClick()

        composeTestRule
            .onNodeWithTag(DeleteSheetDialogTestTags.DELETE_BUTTON)
            .performClick()

        assertThat(deleted).isTrue()
    }

    private val stampedStamp =
        rewardStamp(
            id = 5L,
            sheetId = 1L,
            position = 1,
            stampType = StampType.Shortcake,
        )

    private fun setStampedContent(
        onStampTypeChange: (RewardStamp, StampType) -> Unit = { _, _ -> },
        onStampCancel: (RewardStamp) -> Unit = {},
    ) {
        composeTestRule.setContent {
            SheetDetailContent(
                sheet =
                    rewardSheet(
                        id = 1L,
                        currentCount = 2,
                        goalCount = 10,
                    ),
                stamps =
                    listOf(
                        rewardStamp(id = 4L, sheetId = 1L, position = 0),
                        stampedStamp,
                    ),
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
                onStampTypeChange = onStampTypeChange,
                onStampCancel = onStampCancel,
            )
        }
    }

    // Center of the cell at the position, computed the same way as the board draws it
    private fun TouchInjectionScope.cellCenter(position: Int): Offset {
        val pattern = boardPatterns.getValue(10)
        val metrics =
            BoardLayoutMetrics.build(
                containerHeight = height.toDp(),
                containerWidth = width.toDp(),
                rowNum = pattern.size,
            )
        return buildBoardPoints(
            pattern = pattern,
            metrics = metrics,
            density = this,
            cellNum = 10,
            canvasSize = Size(width.toFloat(), height.toFloat()),
        ).getValue(position)
    }

    private fun longPressStamp(position: Int) {
        composeTestRule
            .onNodeWithTag(RewardBoardViewTestTags.CANVAS)
            .performTouchInput { longClick(cellCenter(position)) }
    }

    @Test
    fun longPressStamp_changesStampType() {
        var changed: Pair<RewardStamp, StampType>? = null
        setStampedContent(
            onStampTypeChange = { stamp, stampType -> changed = stamp to stampType },
        )

        longPressStamp(1)
        composeTestRule
            .onNodeWithTag(StampActionDialogTestTags.CHANGE_BUTTON)
            .performClick()
        composeTestRule
            .onNodeWithTag(StampPickerTestTags.stamp(StampType.Hippopotamus))
            .performClick()

        assertThat(changed).isEqualTo(stampedStamp to StampType.Hippopotamus)
    }

    @Test
    fun longPressStamp_cancelsStamp() {
        var canceled: RewardStamp? = null
        setStampedContent(
            onStampCancel = { canceled = it },
        )

        longPressStamp(1)
        composeTestRule
            .onNodeWithTag(StampActionDialogTestTags.CANCEL_STAMP_BUTTON)
            .performClick()

        assertThat(canceled).isEqualTo(stampedStamp)
        composeTestRule
            .onNodeWithTag(StampActionDialogTestTags.ROOT)
            .assertDoesNotExist()
    }

    @Test
    fun longPressStamp_dismissesWithoutChanges() {
        var called = false
        setStampedContent(
            onStampTypeChange = { _, _ -> called = true },
            onStampCancel = { called = true },
        )

        longPressStamp(1)
        composeTestRule
            .onNodeWithText("キャンセル")
            .performClick()

        assertThat(called).isFalse()
        composeTestRule
            .onNodeWithTag(StampActionDialogTestTags.ROOT)
            .assertDoesNotExist()
    }

    @Test
    fun longPressEmptyCell_doesNothing() {
        setStampedContent()

        longPressStamp(5)

        composeTestRule
            .onNodeWithTag(StampActionDialogTestTags.ROOT)
            .assertDoesNotExist()
    }
}
