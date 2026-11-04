package com.cheesecomer.rewardstamp.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardstamp.ui.component.RewardBoardLayout.EMPTY_CELL
import com.cheesecomer.rewardstamp.ui.component.RewardBoardLayout.GOAL_CELL_SCALE

internal fun buildBoardPoints(
    pattern: List<List<Int>>,
    metrics: BoardLayoutMetrics,
    density: Density,
    cellNum: Int,
    canvasSize: Size,
): Map<Int, Offset> {
    val columnXs =
        listOf(
            canvasSize.width * RewardBoardLayout.FIRST_CELL_RATIO,
            canvasSize.width * RewardBoardLayout.SECOND_CELL_RATIO,
            canvasSize.width * RewardBoardLayout.THIRD_CELL_RATIO,
        )

    val shouldMoveGoal = cellNum >= 5
    val goalOffset =
        if (shouldMoveGoal) {
            with(density) {
                Offset(
                    x = (metrics.cellRadius - metrics.goalCellRadius).toPx(),
                    y = 0f,
                )
            }
        } else {
            Offset.Zero
        }

    val rowHeight: Float
    val topMargin: Float
    with(density) {
        rowHeight = metrics.rowHeight.toPx()
        topMargin = metrics.cellRadius.toPx() + 12.dp.toPx()
    }

    return pattern
        .flatMapIndexed { rowIndex, row ->
            val isLastRow = rowIndex == pattern.lastIndex
            row.mapIndexedNotNull { columnIndex, index ->
                if (index == EMPTY_CELL) {
                    return@mapIndexedNotNull null
                }

                val isGoal = index == cellNum

                val x = columnXs[columnIndex]
                val y =
                    if (isLastRow) {
                        canvasSize.height - ((rowHeight * GOAL_CELL_SCALE) / 2f)
                    } else {
                        topMargin + rowIndex * rowHeight
                    }

                val offset =
                    if (isGoal) {
                        goalOffset
                    } else {
                        Offset.Zero
                    }

                index to Offset(x, y) + offset
            }
        }.toMap()
}
