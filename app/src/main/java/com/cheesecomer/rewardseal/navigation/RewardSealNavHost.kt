package com.cheesecomer.rewardseal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cheesecomer.rewardseal.feature.completedsheet.detail.CompletedSheetDetailScreen
import com.cheesecomer.rewardseal.feature.completedsheet.list.CompletedSheetListScreen
import com.cheesecomer.rewardseal.feature.exchangeablereward.list.ExchangeableRewardListScreen
import com.cheesecomer.rewardseal.feature.sheet.detail.SheetDetailScreen
import com.cheesecomer.rewardseal.feature.sheet.edit.SheetEditScreen
import com.cheesecomer.rewardseal.feature.sheet.list.SheetListScreen

@Suppress("LongMethod")
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
                        Route.sheetDetail(sheetId),
                    )
                },
                onUnreceivedRewardsClick = {
                    navController.navigate(Route.EXCHANGEABLE_REWARD_LIST)
                },
                onCompletedRewardsClick = {
                    navController.navigate(Route.COMPLETED_REWARD_LIST)
                },
            )
        }

        composable(Route.SHEET_EDIT) {
            SheetEditScreen(
                onSaveClick = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(
            route = Route.SHEET_EDIT_WITH_ID,
        ) { backStackEntry ->

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
                        inclusive = false,
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
        composable(
            route = Route.SHEET_DETAIL,
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
                },
                onEditClick = {
                    navController.navigate(Route.sheetEdit(sheetId))
                },
            )
        }
        composable(Route.EXCHANGEABLE_REWARD_LIST) {
            ExchangeableRewardListScreen(
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
        composable(Route.COMPLETED_REWARD_LIST) {
            CompletedSheetListScreen(
                onRewardClick = { completedRewardId ->
                    navController.navigate(
                        Route.completedRewardDetail(completedRewardId),
                    )
                },
            )
        }
        composable(Route.COMPLETED_REWARD_DETAIL) { backStackEntry ->
            val completedRewardId =
                backStackEntry.arguments
                    ?.getString("completedRewardId")
                    ?.toLong()
                    ?: 0L

            CompletedSheetDetailScreen(
                completedRewardId = completedRewardId,
            )
        }
    }
}
