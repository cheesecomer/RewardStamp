package com.cheesecomer.rewardstamp.feature.sheet.edit

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
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
            .onNodeWithTag("SheetEditScreen.TitleTextField")
            .assertExists()

        composeTestRule
            .onNodeWithTag("SheetEditScreen.TitleLabel")
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
            .onNodeWithTag("SheetEditScreen.TitleTextField")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithTag("SheetEditScreen.TitleLabel")
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
            .onNodeWithTag("SheetEditScreen.List")
            .performScrollToNode(hasTestTag("SheetEditScreen.Save"))

        composeTestRule
            .onNodeWithTag("SheetEditScreen.Save")
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
            .onNodeWithTag("SheetEditScreen.List")
            .performScrollToNode(hasTestTag("SheetEditScreen.Save"))

        composeTestRule
            .onNodeWithTag("SheetEditScreen.Save")
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
            .onNodeWithTag("SheetEditScreen.List")
            .performScrollToNode(hasTestTag("SheetEditScreen.Save"))

        composeTestRule
            .onNodeWithTag("SheetEditScreen.Save")
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
            .onNodeWithTag("MilestoneFormList.addMilestoneButton")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag("RewardMilestonesSection.RewardMilestoneDialog.Text.RequiredSheetCount")
            .performTextInput("1")

        composeTestRule
            .onNodeWithTag("RewardMilestonesSection.RewardMilestoneDialog.Text.Reward")
            .performTextInput("アイス")

        composeTestRule
            .onNodeWithTag("RewardMilestonesSection.RewardMilestoneDialog.Confirm")
            .performClick()

        assertThat(addMilestone).isTrue()
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
            .onNodeWithTag("SheetEditScreen.TitleTextField")
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
            .onNodeWithTag("GoalCountPicker.IncrementButton")
            .performClick()

        assertThat(plus).isTrue()
        assertThat(minus).isFalse()
        plus = false

        composeTestRule
            .onNodeWithTag("GoalCountPicker.DecrementButton")
            .performClick()

        assertThat(plus).isFalse()
        assertThat(minus).isTrue()
    }
}
