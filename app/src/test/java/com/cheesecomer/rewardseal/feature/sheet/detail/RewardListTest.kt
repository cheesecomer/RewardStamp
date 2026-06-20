package com.cheesecomer.rewardseal.feature.sheet.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.cheesecomer.rewardseal.data.rewardMilestone
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RewardListTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun notDisplays_WhenHasNotExchangeableRewardsAndHasNotLockedRewards() {
        composeTestRule.setContent {
            Scaffold {
                Box(modifier = Modifier.padding(it)) {
                    RewardList(
                        exchangeableRewards = listOf(),
                        lockedRewards = emptyList(),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("RewardList.ExchangeableRewardsCard").assertDoesNotExist()
        composeTestRule.onNodeWithTag("RewardList.LockedRewardsCard").assertDoesNotExist()
    }

    @Test
    fun displaysLockedRewardsCard_WhenHasLockedRewards() {
        composeTestRule.setContent {
            Scaffold {
                Box(modifier = Modifier.padding(it)) {
                    RewardList(
                        exchangeableRewards = emptyList(),
                        lockedRewards =
                            listOf(
                                rewardMilestone(),
                            ),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("RewardList.ExchangeableRewardsCard").assertDoesNotExist()
        composeTestRule.onNodeWithTag("RewardList.LockedRewardsCard").assertExists()
    }

    @Test
    fun displaysExchangeableRewardsCard_WhenHasExchangeableRewards() {
        composeTestRule.setContent {
            Scaffold {
                Box(modifier = Modifier.padding(it)) {
                    RewardList(
                        exchangeableRewards =
                            listOf(
                                rewardMilestone(),
                            ),
                        lockedRewards = emptyList(),
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("RewardList.ExchangeableRewardsCard").assertExists()
        composeTestRule.onNodeWithTag("RewardList.LockedRewardsCard").assertDoesNotExist()
    }
}
