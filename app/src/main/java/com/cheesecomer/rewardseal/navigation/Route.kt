package com.cheesecomer.rewardseal.navigation

object Route {
    const val SHEET_LIST = "sheets"
    const val SHEET_NEW = "sheets/new"
    const val SHEET_EDIT = "sheets/{sheetId}/edit"
    const val SHEET_DETAIL = "sheets/{sheetId}"

    const val EXCHANGEABLE_SHEET_LIST = "sheets/exchangeable"
    const val COMPLETED_SHEET_LIST = "sheets/completed"
    const val COMPLETED_SHEET_DETAIL = "sheets/completed/{completedSheetId}"

    fun sheetDetail(sheetId: Long): String = "sheets/$sheetId"

    fun sheetEdit(sheetId: Long): String = "sheets/$sheetId/edit"

    fun completedSheetDetail(completedSheetId: Long): String = "sheets/completed/$completedSheetId"
}
