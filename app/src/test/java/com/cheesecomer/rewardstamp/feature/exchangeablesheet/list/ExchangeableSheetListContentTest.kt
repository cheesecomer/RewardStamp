package com.cheesecomer.rewardstamp.feature.exchangeablesheet.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardstamp.data.exchangeableSheet
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExchangeableSheetListContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysEmptyMessage() {
        composeTestRule.setContent {
            RewardStampTheme {
                ExchangeableSheetListContent(
                    sheets = emptyList(),
                )
            }
        }

        composeTestRule
            .onNodeWithTag("ExchangeableRewardListScreen.EmptyList")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("ExchangeableRewardListScreen.List")
            .assertDoesNotExist()
    }

    @Test
    fun displaysExchangeableSheets() {
        composeTestRule.setContent {
            RewardStampTheme {
                ExchangeableSheetListContent(
                    sheets =
                        listOf(
                            exchangeableSheet(
                                title = "はみがき",
                                unconsumedCompletedCount = 3,
                                exchangeableMilestones =
                                    listOf(
                                        rewardMilestone(
                                            requiredSheetCount = 1,
                                            reward = "シール",
                                        ),
                                        rewardMilestone(
                                            requiredSheetCount = 3,
                                            reward = "アイス",
                                        ),
                                    ),
                                nextMilestone =
                                    rewardMilestone(
                                        requiredSheetCount = 5,
                                        reward = "おもちゃ",
                                    ),
                            ),
                        ),
                )
            }
        }

        composeTestRule
            .onNodeWithTag("ExchangeableRewardListScreen.List")
            .assertExists()
    }

    @Test
    fun doesNotDisplayNextMilestoneMessageWhenNextMilestoneIsNull() {
        composeTestRule.setContent {
            RewardStampTheme {
                ExchangeableSheetListContent(
                    sheets =
                        listOf(
                            exchangeableSheet(
                                nextMilestone = null,
                            ),
                        ),
                )
            }
        }

        composeTestRule
            .onNodeWithText("あと", substring = true)
            .assertDoesNotExist()
    }

    @Test
    fun clickReceiveRewardOnSingleMilestone_callsCallback() {
        var clickedSheetId: Long? = null
        val sheet =
            exchangeableSheet(
                id = 123L,
                title = "はみがき",
                exchangeableMilestones =
                    listOf(
                        rewardMilestone(
                            reward = "アイス",
                        ),
                    ),
            )

        composeTestRule.setContent {
            RewardStampTheme {
                ExchangeableSheetListContent(
                    sheets = listOf(sheet),
                    onRewardSelect = { sheetId, milestone ->
                        clickedSheetId = sheetId
                    },
                )
            }
        }

        composeTestRule
            .onNodeWithTag("ExchangeableSheetCard.${sheet.rewardSheet.id}.ExchangeButton")
            .performClick()

        composeTestRule
            .onNodeWithTag("DialogButtons.ConfirmButton")
            .performClick()

        assertThat(clickedSheetId).isEqualTo(sheet.rewardSheet.id)
    }

    @Test
    fun clickReceiveRewardOnSingleMilestone_displaysRewardDialog() {
        var clickedSheetId: Long? = null
        val sheet =
            exchangeableSheet(
                id = 123L,
                title = "はみがき",
                exchangeableMilestones =
                    listOf(
                        rewardMilestone(
                            reward = "アイス",
                        ),
                    ),
            )
        composeTestRule.setContent {
            RewardStampTheme {
                ExchangeableSheetListContent(
                    sheets = listOf(sheet),
                    onRewardSelect = { sheetId, milestone ->
                        clickedSheetId = sheetId
                    },
                )
            }
        }

        composeTestRule
            .onNodeWithTag("ExchangeableSheetCard.${sheet.rewardSheet.id}.ExchangeButton")
            .performClick()

        composeTestRule
            .onNodeWithTag("ExchangeDialog")
            .assertIsDisplayed()

        assertThat(clickedSheetId).isNull()
    }

    @Test
    fun clickReceiveRewardOnMultipleMilestone_displaysRewardDialog() {
        var clickedSheetId: Long? = null
        val sheet =
            exchangeableSheet(
                id = 123L,
                title = "はみがき",
                exchangeableMilestones =
                    listOf(
                        rewardMilestone(
                            reward = "アイス",
                        ),
                        rewardMilestone(
                            reward = "アイス",
                        ),
                    ),
            )
        composeTestRule.setContent {
            RewardStampTheme {
                ExchangeableSheetListContent(
                    sheets = listOf(sheet),
                    onRewardSelect = { sheetId, milestone ->
                        clickedSheetId = sheetId
                    },
                )
            }
        }

        composeTestRule
            .onNodeWithTag("ExchangeableSheetCard.${sheet.rewardSheet.id}.ExchangeButton")
            .performClick()

        composeTestRule
            .onNodeWithTag("ChoiceRewardDialog")
            .assertIsDisplayed()

        assertThat(clickedSheetId).isNull()
    }

    @Test
    fun clickReceiveRewardOnMultipleMilestone_callsCallback() {
        var clickedSheetId: Long? = null
        val sheet =
            exchangeableSheet(
                id = 123L,
                title = "はみがき",
                exchangeableMilestones =
                    listOf(
                        rewardMilestone(
                            id = 1001L,
                            reward = "アイス",
                        ),
                        rewardMilestone(
                            id = 1002L,
                            reward = "アイス",
                        ),
                    ),
            )

        composeTestRule.setContent {
            RewardStampTheme {
                ExchangeableSheetListContent(
                    sheets = listOf(sheet),
                    onRewardSelect = { sheetId, milestone ->
                        clickedSheetId = sheetId
                    },
                )
            }
        }

        composeTestRule
            .onNodeWithTag("ExchangeableSheetCard.${sheet.rewardSheet.id}.ExchangeButton")
            .performClick()
        composeTestRule
            .onNodeWithTag("ChoiceRewardDialog.Rewards.1002")
            .performClick()
        composeTestRule
            .onNodeWithTag("DialogButtons.ConfirmButton")
            .performClick()

        assertThat(clickedSheetId).isEqualTo(sheet.rewardSheet.id)
    }

    private fun rewardMilestone(
        id: Long = 1L,
        sheetId: Long = 1L,
        requiredSheetCount: Int = 1,
        reward: String = "ごほうび",
    ): RewardMilestone =
        RewardMilestone(
            id = id,
            sheetId = sheetId,
            requiredSheetCount = requiredSheetCount,
            reward = reward,
        )
}
