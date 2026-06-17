package com.cheesecomer.rewardseal.feature.exchangeablereward.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardseal.model.ExchangeableSheet
import com.cheesecomer.rewardseal.model.RewardMilestone
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ExchangeableRewardListContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysEmptyMessage() {
        composeTestRule.setContent {
            RewardSealTheme {
                ExchangeableRewardListContent(
                    sheets = emptyList(),
                )
            }
        }

        composeTestRule
            .onNodeWithText("交換できるごほうびはありません")
            .assertIsDisplayed()
    }

    @Test
    fun displaysExchangeableSheets() {
        composeTestRule.setContent {
            RewardSealTheme {
                ExchangeableRewardListContent(
                    sheets =
                        listOf(
                            exchangeableSheet(
                                title = "はみがき",
                                unconsumedCompletedCount = 3,
                                exchangeableMilestones =
                                    listOf(
                                        rewardMilestone(
                                            requiredCompletions = 1,
                                            reward = "シール",
                                        ),
                                        rewardMilestone(
                                            requiredCompletions = 3,
                                            reward = "アイス",
                                        ),
                                    ),
                                nextMilestone =
                                    rewardMilestone(
                                        requiredCompletions = 5,
                                        reward = "おもちゃ",
                                    ),
                            ),
                        ),
                )
            }
        }

        composeTestRule.onNodeWithText("交換できるごほうび").assertIsDisplayed()
        composeTestRule.onNodeWithText("はみがき").assertIsDisplayed()
        composeTestRule.onNodeWithText("未交換シート 3枚").assertIsDisplayed()
        composeTestRule.onNodeWithText("交換可能").assertIsDisplayed()
        composeTestRule.onNodeWithText("1枚 シール").assertIsDisplayed()
        composeTestRule.onNodeWithText("3枚 アイス").assertIsDisplayed()
        composeTestRule.onNodeWithText("あと 2枚 で おもちゃ と交換できるよ").assertIsDisplayed()
        composeTestRule.onNodeWithText("受け取った").assertIsDisplayed()
    }

    @Test
    fun doesNotDisplayNextMilestoneMessageWhenNextMilestoneIsNull() {
        composeTestRule.setContent {
            RewardSealTheme {
                ExchangeableRewardListContent(
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
    fun clickBack_callsCallback() {
        var clicked = false

        composeTestRule.setContent {
            RewardSealTheme {
                ExchangeableRewardListContent(
                    sheets = emptyList(),
                    onBackClick = {
                        clicked = true
                    },
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("戻る")
            .performClick()

        assertThat(clicked).isTrue()
    }

    @Test
    fun clickReceiveReward_callsCallback() {
        var clickedSheetId: Long? = null
        val sheet =
            exchangeableSheet(
                id = 123L,
                title = "はみがき",
            )

        composeTestRule.setContent {
            RewardSealTheme {
                ExchangeableRewardListContent(
                    sheets = listOf(sheet),
                    onRewardSelect = { sheetId, milestone ->
                        clickedSheetId = sheetId
                    },
                )
            }
        }

        composeTestRule
            .onNodeWithText("受け取った")
            .performClick()

        composeTestRule
            .onNodeWithText("交換する")
            .performClick()

        assertThat(clickedSheetId).isEqualTo(sheet.id)
    }

    @Test
    fun clickReceiveReward_displaysRewardDialog() {
        composeTestRule.setContent {
            RewardSealTheme {
                ExchangeableRewardListContent(
                    sheets =
                        listOf(
                            exchangeableSheet(
                                title = "はみがき",
                                exchangeableMilestones =
                                    listOf(
                                        rewardMilestone(
                                            reward = "アイス",
                                        ),
                                    ),
                            ),
                        ),
                )
            }
        }

        composeTestRule
            .onNodeWithText("受け取った")
            .performClick()

        composeTestRule
            .onNodeWithText("交換しますか？")
            .assertIsDisplayed()
    }

    private fun exchangeableSheet(
        id: Long = 1L,
        title: String = "ごほうびシート",
        unconsumedCompletedCount: Int = 1,
        exchangeableMilestones: List<RewardMilestone> =
            listOf(
                rewardMilestone(
                    requiredCompletions = 1,
                    reward = "アイス",
                ),
            ),
        nextMilestone: RewardMilestone? = null,
    ): ExchangeableSheet =
        ExchangeableSheet(
            id = id,
            title = title,
            unconsumedCompletedCount = unconsumedCompletedCount,
            exchangeableMilestones = exchangeableMilestones,
            nextMilestone = nextMilestone,
        )

    private fun rewardMilestone(
        id: Long = 1L,
        sheetId: Long = 1L,
        requiredCompletions: Int = 1,
        reward: String = "ごほうび",
    ): RewardMilestone =
        RewardMilestone(
            id = id,
            sheetId = sheetId,
            requiredCompletions = requiredCompletions,
            reward = reward,
        )
}
