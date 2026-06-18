package com.cheesecomer.rewardseal.navigation

import androidx.navigation.NavController
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage

interface Navigator {
    fun toSheetList()

    fun toSheetNew()

    fun toSheetDetail(sheetId: Long)

    fun toSheetEdit(sheetId: Long)

    fun toCompletedSheetDetail(completedSheetId: Long)

    fun back()
}

@ExcludeFromCoverage
class NavControllerNavigator(
    private val navController: NavController,
) : Navigator {
    override fun toSheetList() {
        navController.navigate(Route.SHEET_LIST) {
            launchSingleTop = true
        }
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

    override fun toCompletedSheetDetail(completedSheetId: Long) {
        navController.navigate(Route.completedSheetDetail(completedSheetId))
    }

    override fun back() {
        navController.popBackStack()
    }
}
