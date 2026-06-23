package com.cheesecomer.rewardstamp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.navigation.BottomTab
import com.cheesecomer.rewardstamp.navigation.NavControllerNavigator
import com.cheesecomer.rewardstamp.navigation.RewardStampNavHost
import com.cheesecomer.rewardstamp.navigation.Route
import com.cheesecomer.rewardstamp.ui.component.RewardStampBottomBar

@ExcludeFromCoverage
@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navigator = NavControllerNavigator(navController)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedTab = Route.bottomTab(backStackEntry?.destination?.route) ?: BottomTab.Sheets

    Scaffold(
        modifier = modifier,
        bottomBar = {
            RewardStampBottomBar(
                selectedTab = selectedTab,
                onTabClick = { tab ->
                    when (tab) {
                        BottomTab.Sheets -> navigator.toSheetList()
                        BottomTab.Rewards -> navigator.toExchangeableRewardList()
                        BottomTab.History -> navigator.toCompletedSheetList()
                        BottomTab.Settings -> navigator.toSettings()
                    }
                },
            )
        },
    ) { innerPadding ->
        RewardStampNavHost(
            navController = navController,
            navigator = NavControllerNavigator(navController),
            modifier = Modifier.padding(innerPadding),
        )
    }
}
