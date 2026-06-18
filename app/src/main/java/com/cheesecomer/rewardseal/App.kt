package com.cheesecomer.rewardseal

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.navigation.NavControllerNavigator
import com.cheesecomer.rewardseal.navigation.RewardSealNavHost
import com.cheesecomer.rewardseal.ui.component.RewardSealBottomBar

@ExcludeFromCoverage
@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            RewardSealBottomBar(
                navController = navController,
            )
        },
    ) { innerPadding ->
        RewardSealNavHost(
            navController = navController,
            navigator = NavControllerNavigator(navController),
            modifier = Modifier.padding(innerPadding),
        )
    }
}
