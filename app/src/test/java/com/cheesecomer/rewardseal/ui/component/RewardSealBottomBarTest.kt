package com.cheesecomer.rewardseal.ui.component
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cheesecomer.rewardseal.navigation.Route
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
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
            val navController = rememberNavController()

            RewardSealTheme {
                RewardSealBottomBar(
                    navController = navController,
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
        composeTestRule.setContent {
            val navController = rememberNavController()

            RewardSealTheme {
                NavHost(
                    navController = navController,
                    startDestination = Route.SHEET_LIST,
                ) {
                    composable(Route.SHEET_LIST) {
                        RewardSealBottomBar(navController)
                    }
                    composable(Route.EXCHANGEABLE_SHEET_LIST) {
                        Text("交換画面")
                    }
                }
            }
        }

        composeTestRule.onNodeWithText("ごほうび").performClick()
        composeTestRule.onNodeWithText("交換画面").assertIsDisplayed()
    }
}
