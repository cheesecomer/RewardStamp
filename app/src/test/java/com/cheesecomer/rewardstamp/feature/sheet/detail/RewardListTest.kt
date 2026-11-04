package com.cheesecomer.rewardstamp.feature.sheet.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.cheesecomer.rewardstamp.data.rewardMilestone
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

        composeTestRule.onNodeWithTag(RewardListTestTags.EXCHANGEABLE_REWARDS_CARD).assertDoesNotExist()
        composeTestRule.onNodeWithTag(RewardListTestTags.LOCKED_REWARDS_CARD).assertDoesNotExist()
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

        composeTestRule.onNodeWithTag(RewardListTestTags.EXCHANGEABLE_REWARDS_CARD).assertDoesNotExist()
        composeTestRule.onNodeWithTag(RewardListTestTags.LOCKED_REWARDS_CARD).assertExists()
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

        composeTestRule.onNodeWithTag(RewardListTestTags.EXCHANGEABLE_REWARDS_CARD).assertExists()
        composeTestRule.onNodeWithTag(RewardListTestTags.LOCKED_REWARDS_CARD).assertDoesNotExist()
    }
}
