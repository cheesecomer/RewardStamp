package com.cheesecomer.rewardstamp.navigation

import androidx.navigation.NavController
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage

interface Navigator {
    fun toSheetList()

    fun toSheetNew()

    fun toSheetDetail(sheetId: Long)

    fun toSheetEdit(sheetId: Long)

    fun toExchangeableRewardList()

    fun toCompletedSheetList()

    fun toCompletedSheetDetail(completedSheetId: Long)

    fun toSettings()

    fun back()
}

@ExcludeFromCoverage
private fun NavController.navigateTopLevel(route: String) {
    navigate(route) {
        popUpTo(Route.SHEET_LIST) {
            saveState = false
            inclusive = false
        }
        launchSingleTop = true
        restoreState = false
    }
}

@ExcludeFromCoverage
class NavControllerNavigator(
    private val navController: NavController,
) : Navigator {
    override fun toSheetList() {
        navController.navigateTopLevel(Route.SHEET_LIST)
    }

    override fun toSheetNew() {
        navController.navigate(Route.SHEET_NEW)
    }

    override fun toSheetDetail(sheetId: Long) {
        navController.navigate(Route.sheetDetail(sheetId))
    }

    override fun toSheetEdit(sheetId: Long) {
        navController.navigate(Route.sheetEdit(sheetId))
    }

    override fun toExchangeableRewardList() {
        navController.navigateTopLevel(Route.EXCHANGEABLE_SHEET_LIST)
    }

    override fun toCompletedSheetList() {
        navController.navigateTopLevel(Route.COMPLETED_SHEET_LIST)
    }

    override fun toCompletedSheetDetail(completedSheetId: Long) {
        navController.navigate(Route.completedSheetDetail(completedSheetId))
    }

    override fun toSettings() {
        navController.navigateTopLevel(Route.SETTINGS)
    }

    override fun back() {
        navController.popBackStack()
    }
}
