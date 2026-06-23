package com.cheesecomer.rewardstamp.navigation

enum class BottomTab {
    Sheets,
    Rewards,
    History,
    Settings,
}

object Route {
    const val SHEET_LIST = "sheets"
    const val SHEET_NEW = "sheets/new"
    const val SHEET_EDIT = "sheets/{sheetId}/edit"
    const val SHEET_DETAIL = "sheets/{sheetId}"

    const val EXCHANGEABLE_SHEET_LIST = "sheets/exchangeable"
    const val COMPLETED_SHEET_LIST = "sheets/completed"
    const val COMPLETED_SHEET_DETAIL = "sheets/completed/{completedSheetId}"

    const val SETTINGS = "settings"

    fun sheetDetail(sheetId: Long): String = "sheets/$sheetId"

    fun sheetEdit(sheetId: Long): String = "sheets/$sheetId/edit"

    fun completedSheetDetail(completedSheetId: Long): String = "sheets/completed/$completedSheetId"

    fun bottomTab(route: String?): BottomTab? =
        when (route) {
            SHEET_LIST,
            SHEET_NEW,
            SHEET_EDIT,
            SHEET_DETAIL,
            -> BottomTab.Sheets

            EXCHANGEABLE_SHEET_LIST -> BottomTab.Rewards

            COMPLETED_SHEET_LIST,
            COMPLETED_SHEET_DETAIL,
            -> BottomTab.History

            SETTINGS -> BottomTab.Settings

            else -> null
        }
}
