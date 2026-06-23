package com.cheesecomer.rewardstamp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Suppress("LongMethod")
@Composable
fun RewardStampNavHost(
    navController: NavHostController,
    navigator: Navigator,
    modifier: Modifier = Modifier,
    screenFactory: ScreenFactory = RewardStampScreenFactory,
) {
    NavHost(
        navController = navController,
        startDestination = Route.SHEET_LIST,
        modifier = modifier,
    ) {
        composable(Route.SHEET_LIST) { backStackEntry ->
            screenFactory.SheetList(
                onSheetClick = navigator::toSheetDetail,
                onCreateSheetClick = navigator::toSheetNew,
            )
        }

        composable(Route.SHEET_NEW) {
            screenFactory.SheetEdit(
                sheetId = null,
                onSaveClick = navigator::back,
                onBackClick = navigator::back,
            )
        }

        composable(
            route = Route.SHEET_EDIT,
        ) { backStackEntry ->

            val sheetId =
                backStackEntry.arguments
                    ?.getString("sheetId")
                    ?.toLong()
                    ?: 0L
            screenFactory.SheetEdit(
                sheetId = sheetId,
                onSaveClick = {
                    navController.popBackStack(
                        route = Route.SHEET_LIST,
                        inclusive = false,
                    )
                },
                onBackClick = navigator::back,
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

            screenFactory.SheetDetail(
                sheetId = sheetId,
                onDeleteClick = navigator::back,
                onRestartWithEditClick = { navigator.toSheetEdit(sheetId) },
                onBackClick = navigator::back,
                onEditClick = { navigator.toSheetEdit(sheetId) },
            )
        }
        composable(Route.EXCHANGEABLE_SHEET_LIST) {
            screenFactory.ExchangeableRewardList()
        }
        composable(Route.COMPLETED_SHEET_LIST) {
            screenFactory.CompletedSheetList(
                onRewardClick = navigator::toCompletedSheetDetail,
            )
        }
        composable(Route.COMPLETED_SHEET_DETAIL) { backStackEntry ->
            val completedSheetId =
                backStackEntry.arguments
                    ?.getString("completedSheetId")
                    ?.toLong()
                    ?: 0L

            screenFactory.CompletedSheetDetail(
                completedSheetId = completedSheetId,
            )
        }
        composable(Route.SETTINGS) {
            screenFactory.Settings()
        }
    }
}
