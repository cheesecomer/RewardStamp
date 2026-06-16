package com.cheesecomer.rewardseal.navigation

object Route {
    const val SHEET_LIST = "sheet_list"
    const val SHEET_EDIT = "sheet_edit"
    const val SHEET_EDIT_WITH_ID = "sheet_edit/{sheetId}"
    const val SHEET_DETAIL = "sheet_detail/{sheetId}"
    const val EXCHANGEABLE_REWARD_LIST = "exchangeable_reward_list"
    const val COMPLETED_REWARD_LIST = "completed_reward_list"
    const val COMPLETED_REWARD_DETAIL = "completed_reward_detail/{completedRewardId}"

    fun sheetDetail(sheetId: Long): String = "sheet_detail/$sheetId"

    fun sheetEdit(sheetId: Long): String = "sheet_edit/$sheetId"

    fun completedRewardDetail(completedRewardId: Long): String = "completed_reward_detail/$completedRewardId"
}
