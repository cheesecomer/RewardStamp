package com.cheesecomer.rewardseal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cheesecomer.rewardseal.ui.screen.completedrewarddetail.CompletedRewardDetailScreen
import com.cheesecomer.rewardseal.ui.screen.completedrewardlist.CompletedRewardListScreen
import com.cheesecomer.rewardseal.ui.screen.sheetdetail.SheetDetailScreen
import com.cheesecomer.rewardseal.ui.screen.sheetedit.SheetEditScreen
import com.cheesecomer.rewardseal.ui.screen.sheetlist.SheetListScreen
import com.cheesecomer.rewardseal.ui.screen.unreceivedrewardlist.UnreceivedRewardListScreen

@Composable
fun RewardSealNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Route.SHEET_LIST,
        modifier = modifier,
    ) {
        composable(Route.SHEET_LIST) { backStackEntry ->
            SheetListScreen(
                onSheetClick = { sheetId ->
                    navController.navigate(
                        Route.sheetDetail(sheetId)
                    )
                },
                onUnreceivedRewardsClick = {
                    navController.navigate(Route.UNRECEIVED_REWARD_LIST)
                },
                onCompletedRewardsClick = {
                    navController.navigate(Route.COMPLETED_REWARD_LIST)
                }
            )
        }

        composable(Route.SHEET_EDIT) {
            SheetEditScreen(
                onSaveClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Route.SHEET_EDIT_WITH_ID
        ) {backStackEntry ->

            val sheetId =
                backStackEntry.arguments
                    ?.getString("sheetId")
                    ?.toLong()
                    ?: 0L
            SheetEditScreen(
                sheetId = sheetId,
                onSaveClick = {
                    navController.popBackStack(
                        route = Route.SHEET_LIST,
                        inclusive = false)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Route.SHEET_DETAIL
        ) { backStackEntry ->

            val sheetId =
                backStackEntry.arguments
                    ?.getString("sheetId")
                    ?.toLong()
                    ?: 0L

            SheetDetailScreen(
                sheetId = sheetId,
                onDeleteClick = {
                    navController.popBackStack()
                },
                onRestartWithEditClick = { id ->
                    navController.navigate(Route.sheetEdit(id))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Route.UNRECEIVED_REWARD_LIST) {
            UnreceivedRewardListScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRestartWithEditClick = { sheetId ->
                    navController.navigate(Route.sheetEdit(sheetId))
                }
            )
        }
        composable(Route.COMPLETED_REWARD_LIST) {
            CompletedRewardListScreen(
                onRewardClick = { completedRewardId ->
                    navController.navigate(
                        Route.completedRewardDetail(completedRewardId)
                    )
                }
            )
        }
        composable(Route.COMPLETED_REWARD_DETAIL) { backStackEntry ->
            val completedRewardId =
                backStackEntry.arguments
                    ?.getString("completedRewardId")
                    ?.toLong()
                    ?: 0L

            CompletedRewardDetailScreen(
                completedRewardId = completedRewardId
            )
        }
    }
}