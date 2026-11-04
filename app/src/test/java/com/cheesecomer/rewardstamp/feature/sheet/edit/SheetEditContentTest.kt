package com.cheesecomer.rewardstamp.feature.sheet.edit

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.cheesecomer.rewardstamp.data.rewardMilestone
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SheetEditContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun createMode_displaysCreateTitle() {
        composeTestRule.setContent {
            SheetEditContent(
                sheetId = null,
                title = "",
                goalCount = 10,
                milestones =
                    listOf(
                        rewardMilestone(
                            requiredSheetCount = 1,
                        ),
                    ),
                canSave = false,
                onUpdateMilestoneClick = { _, _ -> },
                onCreateMilestoneClick = {},
                onRemoveMilestoneClick = {},
            )
        }

        composeTestRule
            .onNodeWithText("シートを作る")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(SheetEditTestTags.TITLE_TEXT_FIELD)
            .assertExists()

        composeTestRule
            .onNodeWithTag(SheetEditTestTags.TITLE_LABEL)
            .assertDoesNotExist()
    }

    @Test
    fun editMode_displaysEditTitle() {
        composeTestRule.setContent {
            SheetEditContent(
                sheetId = 1L,
                title = "はみがき",
                goalCount = 10,
                milestones =
                    listOf(
                        rewardMilestone(
                            requiredSheetCount = 1,
                        ),
                    ),
                canSave = false,
                onUpdateMilestoneClick = { _, _ -> },
                onCreateMilestoneClick = {},
                onRemoveMilestoneClick = {},
            )
        }

        composeTestRule
            .onNodeWithText("シートを編集")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(SheetEditTestTags.TITLE_TEXT_FIELD)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag(SheetEditTestTags.TITLE_LABEL)
            .assertExists()
    }

    @Test
    fun saveButton_isDisabled() {
        composeTestRule.setContent {
            SheetEditContent(
                sheetId = null,
                title = "",
                goalCount = 10,
                milestones =
                    listOf(
                        rewardMilestone(
                            requiredSheetCount = 1,
                        ),
                    ),
                canSave = false,
                onUpdateMilestoneClick = { _, _ -> },
                onCreateMilestoneClick = {},
                onRemoveMilestoneClick = {},
            )
        }
        composeTestRule
            .onNodeWithTag(SheetEditTestTags.LIST)
            .performScrollToNode(hasTestTag(SheetEditTestTags.SAVE))

        composeTestRule
            .onNodeWithTag(SheetEditTestTags.SAVE)
            .assertExists()
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun saveButton_isEnabled() {
        composeTestRule.setContent {
            SheetEditContent(
                sheetId = null,
                title = "",
                goalCount = 10,
                milestones = listOf(),
                canSave = true,
                onUpdateMilestoneClick = { _, _ -> },
                onCreateMilestoneClick = {},
                onRemoveMilestoneClick = {},
            )
        }
        composeTestRule
            .onNodeWithTag(SheetEditTestTags.LIST)
            .performScrollToNode(hasTestTag(SheetEditTestTags.SAVE))

        composeTestRule
            .onNodeWithTag(SheetEditTestTags.SAVE)
            .assertExists()
            .performScrollTo()
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    fun clickSave_callsCallback() {
        var saved = false

        composeTestRule.setContent {
            SheetEditContent(
                sheetId = null,
                title = "",
                goalCount = 10,
                milestones = listOf(),
                canSave = true,
                onSaveClick = { saved = true },
                onUpdateMilestoneClick = { _, _ -> },
                onCreateMilestoneClick = {},
                onRemoveMilestoneClick = {},
            )
        }
        composeTestRule
            .onNodeWithTag(SheetEditTestTags.LIST)
            .performScrollToNode(hasTestTag(SheetEditTestTags.SAVE))

        composeTestRule
            .onNodeWithTag(SheetEditTestTags.SAVE)
            .assertExists()
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        assertThat(saved).isTrue()
    }

    @Test
    fun clickBack_callsCallback() {
        var back = false

        composeTestRule.setContent {
            SheetEditContent(
                sheetId = null,
                title = "",
                goalCount = 10,
                milestones =
                    listOf(
                        rewardMilestone(
                            requiredSheetCount = 1,
                        ),
                    ),
                canSave = false,
                onBackClick = { back = true },
                onUpdateMilestoneClick = { _, _ -> },
                onCreateMilestoneClick = {},
                onRemoveMilestoneClick = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("戻る")
            .performClick()

        assertThat(back).isTrue()
    }

    @Test
    fun clickAddMilestone_callsCallback() {
        var addMilestone = false

        composeTestRule.setContent {
            SheetEditContent(
                sheetId = null,
                title = "",
                goalCount = 10,
                milestones =
                    listOf(
                        rewardMilestone(
                            requiredSheetCount = 1,
                        ),
                    ),
                canSave = false,
                onBackClick = { },
                onUpdateMilestoneClick = { _, _ -> },
                onCreateMilestoneClick = {
                    addMilestone = true
                },
                onRemoveMilestoneClick = {},
            )
        }

        composeTestRule
            .onNodeWithTag(RewardMilestonesSectionTestTags.ADD_MILESTONE_BUTTON)
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag(RewardMilestoneDialogTestTags.REQUIRED_SHEET_COUNT)
            .performTextInput("1")

        composeTestRule
            .onNodeWithTag(RewardMilestoneDialogTestTags.REWARD)
            .performTextInput("アイス")

        composeTestRule
            .onNodeWithTag(RewardMilestoneDialogTestTags.CONFIRM)
            .performClick()

        assertThat(addMilestone).isTrue()
    }

    @Test
    fun milestoneDialog_disablesConfirmWhenRequiredSheetCountIsInvalid() {
        composeTestRule.setContent {
            SheetEditContent(
                sheetId = null,
                title = "",
                goalCount = 10,
                milestones = emptyList(),
            )
        }

        composeTestRule
            .onNodeWithTag(RewardMilestonesSectionTestTags.ADD_MILESTONE_BUTTON)
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNodeWithTag(RewardMilestoneDialogTestTags.REWARD)
            .performTextInput("アイス")

        listOf("0", "-1", "1.5", "99999999999").forEach { input ->
            composeTestRule
                .onNodeWithTag(RewardMilestoneDialogTestTags.REQUIRED_SHEET_COUNT)
                .performTextReplacement(input)

            composeTestRule
                .onNodeWithTag(RewardMilestoneDialogTestTags.CONFIRM)
                .assertIsNotEnabled()
        }

        composeTestRule
            .onNodeWithTag(RewardMilestoneDialogTestTags.REQUIRED_SHEET_COUNT)
            .performTextReplacement("1")

        composeTestRule
            .onNodeWithTag(RewardMilestoneDialogTestTags.CONFIRM)
            .assertIsEnabled()
    }

    @Test
    fun titleInput_callsCallback() {
        var title = ""

        composeTestRule.setContent {
            SheetEditContent(
                sheetId = null,
                title = "",
                goalCount = 10,
                milestones =
                    listOf(
                        rewardMilestone(
                            requiredSheetCount = 1,
                        ),
                    ),
                canSave = false,
                onTitleChange = { title = it },
                onUpdateMilestoneClick = { _, _ -> },
                onCreateMilestoneClick = {},
                onRemoveMilestoneClick = {},
            )
        }

        composeTestRule
            .onNodeWithTag(SheetEditTestTags.TITLE_TEXT_FIELD)
            .performTextInput("おかたづけ")

        assertThat(title).isEqualTo("おかたづけ")
    }

    @Test
    fun goalCountButtons_callCallbacks() {
        var plus = false
        var minus = false

        composeTestRule.setContent {
            SheetEditContent(
                sheetId = null,
                title = "",
                goalCount = 10,
                milestones =
                    listOf(
                        rewardMilestone(
                            requiredSheetCount = 1,
                        ),
                    ),
                canSave = false,
                onIncrementGoalCount = { plus = true },
                onDecrementGoalCount = { minus = true },
                onUpdateMilestoneClick = { _, _ -> },
                onCreateMilestoneClick = {},
                onRemoveMilestoneClick = {},
            )
        }

        composeTestRule
            .onNodeWithTag(GoalCountPickerTestTags.INCREMENT_BUTTON)
            .performClick()

        assertThat(plus).isTrue()
        assertThat(minus).isFalse()
        plus = false

        composeTestRule
            .onNodeWithTag(GoalCountPickerTestTags.DECREMENT_BUTTON)
            .performClick()

        assertThat(plus).isFalse()
        assertThat(minus).isTrue()
    }
}
