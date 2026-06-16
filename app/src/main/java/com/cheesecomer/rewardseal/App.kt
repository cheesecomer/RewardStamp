package com.cheesecomer.rewardseal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cheesecomer.rewardseal.navigation.RewardSealNavHost

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    RewardSealNavHost(
        navController = navController,
        modifier = modifier,
    )
}
