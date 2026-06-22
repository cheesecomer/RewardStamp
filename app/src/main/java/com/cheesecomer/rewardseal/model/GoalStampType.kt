package com.cheesecomer.rewardseal.model

import androidx.annotation.DrawableRes
import com.cheesecomer.rewardseal.R

enum class GoalStampType(
    val id: String,
    @DrawableRes val iconRes: Int,
) {
    Bear1("Bear1", R.drawable.stamp_goal_bear_1),
    Bear2("Bear2", R.drawable.stamp_goal_bear_2),
    Bear3("Bear3", R.drawable.stamp_goal_bear_3),
    Bear4("Bear4", R.drawable.stamp_goal_bear_4),
    Bear5("Bear5", R.drawable.stamp_goal_bear_5),
    Bear6("Bear6", R.drawable.stamp_goal_bear_6),
    Bear7("Bear7", R.drawable.stamp_goal_bear_7),
    Bear8("Bear8", R.drawable.stamp_goal_bear_8),
    Bear9("Bear9", R.drawable.stamp_goal_bear_9),
    CherryBlossom1("CherryBlossom1", R.drawable.stamp_goal_cherry_blossom_1),
    CherryBlossom2("CherryBlossom2", R.drawable.stamp_goal_cherry_blossom_2),
    CherryBlossom3("CherryBlossom3", R.drawable.stamp_goal_cherry_blossom_3),
    CherryBlossom4("CherryBlossom4", R.drawable.stamp_goal_cherry_blossom_4),
    CherryBlossom5("CherryBlossom5", R.drawable.stamp_goal_cherry_blossom_5),
    CherryBlossom6("CherryBlossom6", R.drawable.stamp_goal_cherry_blossom_6),
    CherryBlossom7("CherryBlossom7", R.drawable.stamp_goal_cherry_blossom_7),
    CherryBlossom8("CherryBlossom8", R.drawable.stamp_goal_cherry_blossom_8),
    CherryBlossom9("CherryBlossom9", R.drawable.stamp_goal_cherry_blossom_9),
    Clover1("Clover1", R.drawable.stamp_goal_clover_1),
    Clover2("Clover2", R.drawable.stamp_goal_clover_2),
    Clover3("Clover3", R.drawable.stamp_goal_clover_3),
    Heart1("Heart1", R.drawable.stamp_goal_heart_1),
    Heart2("Heart2", R.drawable.stamp_goal_heart_2),
    Heart3("Heart3", R.drawable.stamp_goal_heart_3),
    Apple1("Apple1", R.drawable.stamp_goal_apple_1),
    Apple2("Apple2", R.drawable.stamp_goal_apple_2),
    Apple3("Apple3", R.drawable.stamp_goal_apple_3),
    Star1("Star1", R.drawable.stamp_goal_star_1),
    Star2("Star2", R.drawable.stamp_goal_star_2),
    Strawberry1("Strawberry1", R.drawable.stamp_goal_strawberry_1),
    Strawberry2("Strawberry2", R.drawable.stamp_goal_strawberry_1),
    ;

    companion object {
        fun fromId(id: String): GoalStampType =
            GoalStampType.entries.firstOrNull {
                it.id == id
            } ?: Bear1
    }
}
