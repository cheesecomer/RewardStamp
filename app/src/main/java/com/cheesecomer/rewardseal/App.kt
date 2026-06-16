package com.cheesecomer.rewardseal

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cheesecomer.rewardseal.navigation.RewardSealNavHost
import com.cheesecomer.rewardseal.navigation.Route

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            if (currentRoute == Route.SHEET_LIST) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Route.SHEET_EDIT)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "シートを作る",
                    )
                }
            }
        },
    ) { innerPadding ->
        RewardSealNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
