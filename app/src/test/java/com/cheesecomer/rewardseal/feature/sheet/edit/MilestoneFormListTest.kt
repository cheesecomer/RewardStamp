package com.cheesecomer.rewardseal.feature.sheet.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MilestoneFormListTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setContent(content: @Composable () -> Unit) {
        composeTestRule.setContent {
            Scaffold {
                Box(
                    modifier = Modifier.padding(it),
                ) {
                    content()
                }
            }
        }
    }

    @Test
    fun displaysMilestones() {
        setContent {
            MilestoneFormList(
                milestones =
                    listOf(
                        RewardMilestoneUiState(
                            requiredCompletions = "1",
                            reward = "シール",
                        ),
                        RewardMilestoneUiState(
                            requiredCompletions = "3",
                            reward = "アイス",
                        ),
                    ),
                onRequiredCompletionsChange = { _, _ -> },
                onRewardChange = { _, _ -> },
                onAddClick = {},
                onRemoveClick = {},
            )
        }

        composeTestRule.onNodeWithText("1").assertIsDisplayed()
        composeTestRule.onNodeWithText("3").assertIsDisplayed()
        composeTestRule.onNodeWithText("シール").assertIsDisplayed()
        composeTestRule.onNodeWithText("アイス").assertIsDisplayed()
        composeTestRule.onNodeWithText("＋ ごほうびを追加").assertIsDisplayed()
    }

    @Test
    fun displaysRemoveButtonsWhenMilestonesAreMultiple() {
        setContent {
            MilestoneFormList(
                milestones =
                    listOf(
                        RewardMilestoneUiState(
                            requiredCompletions = "1",
                            reward = "シール",
                        ),
                        RewardMilestoneUiState(
                            requiredCompletions = "3",
                            reward = "アイス",
                        ),
                    ),
                onRequiredCompletionsChange = { _, _ -> },
                onRewardChange = { _, _ -> },
                onAddClick = {},
                onRemoveClick = {},
            )
        }

        composeTestRule
            .onAllNodesWithText("削除")
            .assertCountEquals(2)
    }

    @Test
    fun doesNotDisplayRemoveButtonWhenMilestoneIsSingle() {
        setContent {
            MilestoneFormList(
                milestones =
                    listOf(
                        RewardMilestoneUiState(
                            requiredCompletions = "1",
                            reward = "シール",
                        ),
                    ),
                onRequiredCompletionsChange = { _, _ -> },
                onRewardChange = { _, _ -> },
                onAddClick = {},
                onRemoveClick = {},
            )
        }

        composeTestRule
            .onNodeWithText("削除")
            .assertDoesNotExist()
    }

    @Test
    fun inputRequiredCompletions_callsCallback() {
        var changedIndex: Int? = null
        var changedValue: String? = null

        setContent {
            MilestoneFormList(
                milestones =
                    listOf(
                        RewardMilestoneUiState(
                            requiredCompletions = "",
                            reward = "シール",
                        ),
                    ),
                onRequiredCompletionsChange = { index, value ->
                    changedIndex = index
                    changedValue = value
                },
                onRewardChange = { _, _ -> },
                onAddClick = {},
                onRemoveClick = {},
            )
        }
        composeTestRule
            .onNodeWithTag("MilestoneFormList.requiredCompletionsTextField.0")
            .performTextInput("3")

        assertThat(changedIndex).isEqualTo(0)
        assertThat(changedValue).isEqualTo("3")
    }

    @Test
    fun inputReward_callsCallback() {
        var changedIndex: Int? = null
        var changedValue: String? = null

        setContent {
            MilestoneFormList(
                milestones =
                    listOf(
                        RewardMilestoneUiState(
                            requiredCompletions = "1",
                            reward = "",
                        ),
                    ),
                onRequiredCompletionsChange = { _, _ -> },
                onRewardChange = { index, value ->
                    changedIndex = index
                    changedValue = value
                },
                onAddClick = {},
                onRemoveClick = {},
            )
        }
        composeTestRule
            .onNodeWithTag("MilestoneFormList.rewardTextField.0")
            .performTextInput("アイス")

        assertThat(changedIndex).isEqualTo(0)
        assertThat(changedValue).isEqualTo("アイス")
    }

    @Test
    fun clickAdd_callsCallback() {
        var clicked = false

        setContent {
            MilestoneFormList(
                milestones =
                    listOf(
                        RewardMilestoneUiState(
                            requiredCompletions = "1",
                            reward = "シール",
                        ),
                    ),
                onRequiredCompletionsChange = { _, _ -> },
                onRewardChange = { _, _ -> },
                onAddClick = {
                    clicked = true
                },
                onRemoveClick = {},
            )
        }

        composeTestRule
            .onNodeWithTag("MilestoneFormList.addMilestoneButton")
            .performClick()

        assertThat(clicked).isTrue()
    }

    @Test
    fun clickRemove_callsCallback() {
        var removedIndex: Int? = null

        setContent {
            MilestoneFormList(
                milestones =
                    listOf(
                        RewardMilestoneUiState(
                            requiredCompletions = "1",
                            reward = "シール",
                        ),
                        RewardMilestoneUiState(
                            requiredCompletions = "3",
                            reward = "アイス",
                        ),
                    ),
                onRequiredCompletionsChange = { _, _ -> },
                onRewardChange = { _, _ -> },
                onAddClick = {},
                onRemoveClick = { index ->
                    removedIndex = index
                },
            )
        }

        composeTestRule
            .onAllNodesWithText("削除")[1]
            .performClick()

        assertThat(removedIndex).isEqualTo(1)
    }
}
