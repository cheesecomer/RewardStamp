package com.cheesecomer.rewardstamp.feature.completedsheet.list

internal object CompletedSheetListTestTags {
    const val EMPTY_LIST = "CompletedSheetListScreen.EmptyList"
    const val GRID = "CompletedSheetListScreen.CompletedSheetGrid"

    fun gridItem(sheetId: Long) = "$GRID.$sheetId"

    fun rewardReceivedBadge(sheetId: Long) = "${gridItem(sheetId)}.RewardReceivedBadge"
}
