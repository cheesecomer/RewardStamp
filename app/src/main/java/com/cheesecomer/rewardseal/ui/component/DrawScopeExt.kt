package com.cheesecomer.rewardseal.ui.component

import android.graphics.drawable.Drawable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withSave
import com.cheesecomer.rewardseal.ui.theme.GoalInner
import com.cheesecomer.rewardseal.ui.theme.GoalOuter
import com.cheesecomer.rewardseal.ui.theme.SheetBorder
import com.cheesecomer.rewardseal.ui.theme.SheetDivider
import com.cheesecomer.rewardseal.ui.theme.SheetPrimary

private val ActiveLineColor = SheetPrimary
private val InactiveLineColor = SheetDivider
private const val LINE_WIDTH = 12f

fun DrawScope.drawLines(
    pointsByIndex: Map<Int, Offset>,
    currentCount: Int,
) {
    for (index in 0 until pointsByIndex.keys.max()) {
        val from = pointsByIndex[index] ?: continue
        val to = pointsByIndex[index + 1] ?: continue

        drawLine(
            color =
                if (currentCount == pointsByIndex.keys.max() || index < currentCount - 1) {
                    ActiveLineColor
                } else {
                    InactiveLineColor
                },
            start = from,
            end = to,
            strokeWidth = LINE_WIDTH,
            cap = StrokeCap.Round,
        )
    }
}

fun DrawScope.drawCells(
    pointsByIndex: Map<Int, Offset>,
    metrics: BoardLayoutMetrics,
    board: RewardBoardState,
) {
    pointsByIndex
        .toSortedMap()
        .forEach { (index, center) ->
            when {
                index == board.goalCount -> {
                    drawGoalCell(center = center, metrics = metrics)
                }
                else -> {
                    drawStampCell(center = center, metrics = metrics)
                }
            }
        }
}

private fun DrawScope.drawGoalCell(
    center: Offset,
    metrics: BoardLayoutMetrics,
) {
    val cellRadius = metrics.goalCellRadius.toPx()
    drawCircle(
        color = Color.White,
        radius = cellRadius,
        center = center,
    )

    drawCircle(
        color = GoalOuter,
        radius = cellRadius,
        center = center,
        style = Stroke(8.dp.toPx()),
    )

    drawCircle(
        color = GoalInner,
        radius = cellRadius - 2.dp.toPx(),
        center = center,
        style = Stroke(4.dp.toPx()),
    )
}

fun DrawScope.drawGoalMark(
    center: Offset,
    drawable: Drawable,
    metrics: BoardLayoutMetrics,
) {
    val iconSizePx = metrics.goalCellRadius.toPx() * 0.8f
    drawIntoCanvas { canvas ->
        val nativeCanvas = canvas.nativeCanvas

        nativeCanvas.withSave {
            val left = (center.x - iconSizePx / 2f).toInt()
            val top = (center.y - iconSizePx / 2f).toInt()
            val right = (left + iconSizePx).toInt()
            val bottom = (top + iconSizePx).toInt()

            drawable.setBounds(
                left,
                top,
                right,
                bottom,
            )

            drawable.setTint(Color.Gray.copy(alpha = 0.5f).toArgb())
            drawable.draw(canvas.nativeCanvas)
        }
    }
}

fun DrawScope.drawStampCell(
    center: Offset,
    metrics: BoardLayoutMetrics,
) {
    val cellRadius = metrics.cellRadius.toPx()

    drawCircle(
        color = Color.White,
        radius = cellRadius,
        center = center,
    )

    drawCircle(
        color = SheetBorder,
        radius = cellRadius,
        center = center,
        style = Stroke(2.dp.toPx()),
    )
}
