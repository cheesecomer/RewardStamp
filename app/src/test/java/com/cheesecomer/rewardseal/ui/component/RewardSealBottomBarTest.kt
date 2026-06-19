package com.cheesecomer.rewardseal.ui.component
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cheesecomer.rewardseal.navigation.BottomTab
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RewardSealBottomBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsBottomNavigationItems() {
        composeTestRule.setContent {
            RewardSealTheme {
                RewardSealBottomBar(
                    selectedTab = BottomTab.Sheets,
                )
            }
        }

        composeTestRule.onNodeWithText("シート").assertIsDisplayed()
        composeTestRule.onNodeWithText("ごほうび").assertIsDisplayed()
        composeTestRule.onNodeWithText("れきし").assertIsDisplayed()
        composeTestRule.onNodeWithText("せってい").assertIsDisplayed()
    }

    @Test
    fun navigatesToRewardsWhenRewardTabClicked() {
        var shouldRewardList = false
        composeTestRule.setContent {
            RewardSealTheme {
                RewardSealBottomBar(
                    selectedTab = BottomTab.Sheets,
                    onTabClick = {
                        shouldRewardList = it == BottomTab.Rewards
                    },
                )
            }
        }

        composeTestRule.onNodeWithText("ごほうび").performClick()
        assertThat(shouldRewardList).isTrue()
    }
}
