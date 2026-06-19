package com.cheesecomer.rewardseal.ui.component

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class BoardLayoutMetrics(
    val canvasHeight: Dp,
    val rowHeight: Dp,
    val cellRadius: Dp,
    val goalCellRadius: Dp,
    val iconSize: Dp,
) {
    companion object {
        fun build(
            containerHeight: Dp,
            containerWidth: Dp,
            rowNum: Int,
        ): BoardLayoutMetrics {
            val rowHeight = containerHeight / RewardBoardLayout.VISIBLE_ROW_COUNT

            val canvasHeight =
                if (rowNum > 5) {
                    (containerHeight / RewardBoardLayout.VISIBLE_ROW_COUNT) * (rowNum + 1)
                } else {
                    containerHeight
                }

            val secondColumnX = containerWidth * RewardBoardLayout.SECOND_CELL_RATIO
            val firstColumnX = containerWidth * RewardBoardLayout.FIRST_CELL_RATIO
            val cellRadiusByWidth = (secondColumnX - firstColumnX) * 0.5f - 15.dp
            val cellRadiusByHeight = (containerHeight / RewardBoardLayout.VISIBLE_ROW_COUNT) * 0.5f - 8.dp

            val cellRadius =
                arrayOf(
                    cellRadiusByWidth,
                    cellRadiusByHeight,
                ).min()
            val goalCellRadius = cellRadius * 1.55f

            val iconSize = cellRadius * 1.45f

            return BoardLayoutMetrics(
                canvasHeight = canvasHeight,
                rowHeight = rowHeight,
                cellRadius = cellRadius,
                iconSize = iconSize,
                goalCellRadius = goalCellRadius,
            )
        }
    }
}
