package com.cheesecomer.rewardseal.ui.component

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.cheesecomer.rewardseal.model.RewardStamp
import com.cheesecomer.rewardseal.model.StampType
import kotlin.random.Random

private const val CONNECTION_OFFSET_RATIO = 0.75f

private fun connectionPoint(
    point: Offset,
    radius: Float,
    isExit: Boolean,
): Offset =
    if (isExit) {
        point + Offset(0f, radius * CONNECTION_OFFSET_RATIO)
    } else {
        point + Offset(0f, -radius * CONNECTION_OFFSET_RATIO)
    }

private const val POINT_OFFSET_RANGE = 40f

private fun pointOffset(index: Int): Float {
    val random = Random(index)

    return random.nextFloat() * (POINT_OFFSET_RANGE * 2) - POINT_OFFSET_RANGE
}

private const val STAMP_OFFSET_RANGE = 15f

private fun stampOffset(position: Int): Pair<Float, Float> {
    val random = Random(position)

    return Pair(
        random.nextFloat() * (STAMP_OFFSET_RANGE * 2) - STAMP_OFFSET_RANGE,
        random.nextFloat() * (STAMP_OFFSET_RANGE * 2) - STAMP_OFFSET_RANGE,
    )
}

data class RewardBoardState(
    val title: String,
    val currentCount: Int,
    val goalCount: Int,
)

data class RewardBoardSize(
    val topMarginCells: Int,
    val bottomMarginCells: Int,
    val goalCount: Int,
    val totalCells: Int,
    val cellHeight: Dp,
    val boardHeight: Dp,
) {
    companion object {
        fun create(
            maxHeight: Dp,
            goalCount: Int,
            visibleCells: Int = 13,
            topMarginCells: Int = 1,
            bottomMarginCells: Int = 2,
        ): RewardBoardSize {
            val totalCells =
                maxOf(
                    visibleCells,
                    goalCount + topMarginCells + bottomMarginCells,
                )

            val cellHeight = maxHeight / visibleCells
            val boardHeight = cellHeight * totalCells

            return RewardBoardSize(
                topMarginCells = topMarginCells,
                bottomMarginCells = bottomMarginCells,
                goalCount = goalCount,
                totalCells = totalCells,
                cellHeight = cellHeight,
                boardHeight = boardHeight,
            )
        }
    }
}

data class RewardBoardLayout(
    val goalCount: Int,
    val cellHeight: Dp,
    val cellHeightPx: Float,
    val topMargin: Float,
    val bottomMargin: Float,
    val nodeRadius: Float,
    val goalRadius: Float,
    val strokeWidth: Float,
    val leftMargin: Float,
    val rightMargin: Float,
    val drawableWidth: Float,
    val drawableHeight: Float,
    val stepY: Float,
    val centerX: Float,
    val amplitude: Float,
    val points: List<Offset>,
) {
    companion object {
        fun create(
            canvasSize: Size,
            boardSize: RewardBoardSize,
        ): RewardBoardLayout {
            val cellHeightPx = canvasSize.height / boardSize.totalCells

            val topMargin = cellHeightPx * boardSize.topMarginCells
            val bottomMargin = cellHeightPx * boardSize.bottomMarginCells

            val nodeRadius = cellHeightPx * 0.22f
            val goalRadius = cellHeightPx * 0.34f
            val strokeWidth = cellHeightPx * 0.06f

            val leftMargin = cellHeightPx * 0.3f
            val rightMargin = cellHeightPx * 0.3f

            val drawableWidth = canvasSize.width - leftMargin - rightMargin
            val drawableHeight = canvasSize.height - topMargin - bottomMargin

            val stepY = drawableHeight / (boardSize.goalCount - 1)

            val centerX = leftMargin + drawableWidth / 2f
            val amplitude = drawableWidth * 0.36f

            val points =
                (0 until boardSize.goalCount).map { index ->
                    Offset(
                        x = centerX + pointOffset(index) + if (index % 2 == 0) -amplitude else amplitude,
                        y = topMargin + index * stepY,
                    )
                }

            return RewardBoardLayout(
                goalCount = boardSize.goalCount,
                cellHeight = boardSize.cellHeight,
                cellHeightPx = cellHeightPx,
                topMargin = topMargin,
                bottomMargin = bottomMargin,
                nodeRadius = nodeRadius,
                goalRadius = goalRadius,
                strokeWidth = strokeWidth,
                leftMargin = leftMargin,
                rightMargin = rightMargin,
                drawableWidth = drawableWidth,
                drawableHeight = drawableHeight,
                stepY = stepY,
                centerX = centerX,
                amplitude = amplitude,
                points = points,
            )
        }
    }
}

private fun DrawScope.drawBoardLines(layout: RewardBoardLayout) {
    for (i in 0 until layout.points.lastIndex) {
        val startCenter = layout.points[i]
        val endCenter = layout.points[i + 1]

        val startRadius =
            if (i == layout.goalCount - 1) layout.goalRadius else layout.nodeRadius

        val endRadius =
            if (i + 1 == layout.goalCount - 1) layout.goalRadius else layout.nodeRadius

        val start =
            if (i == 0) {
                startCenter
            } else {
                connectionPoint(
                    point = startCenter,
                    radius = startRadius,
                    isExit = true,
                )
            }

        val end =
            if (i + 1 == layout.goalCount - 1) {
                endCenter
            } else {
                connectionPoint(
                    point = endCenter,
                    radius = endRadius,
                    isExit = false,
                )
            }

        val direction = if (i % 2 == 0) 1f else -1f
        val bend = layout.cellHeightPx * 1.6f * direction
        val verticalBend = layout.stepY * 0.25f

        val path =
            Path().apply {
                moveTo(start.x, start.y)

                cubicTo(
                    start.x + bend,
                    start.y + verticalBend,
                    end.x - bend,
                    end.y - verticalBend,
                    end.x,
                    end.y,
                )
            }

        drawPath(
            path = path,
            color = Color.Gray.copy(alpha = 0.25f),
            style =
                Stroke(
                    width = layout.strokeWidth * 1.25f,
                    cap = StrokeCap.Round,
                ),
        )
    }
}

val GoalNodeColor = Color(0xFFFFD54F)
val StampedNodeColor = Color(0xFFFF8A80)

private fun DrawScope.drawBoardNodes(
    layout: RewardBoardLayout,
    currentCount: Int,
) {
    layout.points.forEachIndexed { index, point ->
        val isStamped = index < currentCount
        val isGoal = index == layout.goalCount - 1
        val radius = if (isGoal) layout.goalRadius else layout.nodeRadius

        drawCircle(
            color =
                when {
                    isGoal -> GoalNodeColor
                    isStamped -> StampedNodeColor
                    else -> Color.White
                },
            radius = radius,
            center = point,
        )

        drawCircle(
            color = Color.Black,
            radius = radius,
            center = point,
            style = Stroke(width = layout.strokeWidth * 1.5f),
        )
    }
}

private fun DrawScope.drawBoardStamps(
    layout: RewardBoardLayout,
    stamps: List<RewardStamp>,
    stampDrawables: Map<StampType, Drawable>,
) {
    layout.points.forEachIndexed { index, point ->
        val stamp = stamps.firstOrNull { it.position == index }
        if (stamp != null) {
            val (dx, dy) = stampOffset(stamp.position)
            val drawable = stampDrawables[stamp.stampType]

            if (drawable != null) {
                val iconSize = (layout.cellHeight * 1.5f).roundToPx()
                val rotation =
                    Random(stamp.position)
                        .nextFloat() * 20f - 10f
                val direction =
                    if (index % 2 == 0) {
                        -1
                    } else {
                        1
                    }

                val left =
                    (
                        point.x -
                            iconSize / 2f +
                            direction * iconSize * 0.2f +
                            dx
                    ).toInt()

                val top =
                    (
                        point.y -
                            iconSize / 2f +
                            dy
                    ).toInt()

                drawIntoCanvas { canvas ->
                    canvas.save()

                    canvas.nativeCanvas.rotate(
                        rotation,
                        point.x,
                        point.y,
                    )

                    drawable.setBounds(
                        left,
                        top,
                        left + iconSize,
                        top + iconSize,
                    )

                    drawable.draw(canvas.nativeCanvas)

                    canvas.restore()
                }
            }
        }
    }
}

@Composable
fun RewardBoardView(
    board: RewardBoardState,
    stamps: List<RewardStamp>,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val stampDrawables =
        remember {
            StampType.entries.associateWith { stampType ->
                ContextCompat.getDrawable(context, stampType.iconRes)!!
            }
        }
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        val boardSize =
            RewardBoardSize.create(
                this.maxHeight,
                board.goalCount,
            )
        val scrollState = rememberScrollState()

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
        ) {
            Canvas(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(boardSize.boardHeight)
                        .padding(horizontal = 24.dp),
            ) {
                val layout = RewardBoardLayout.create(size, boardSize)

                drawBoardLines(layout)
                drawBoardNodes(layout, board.currentCount)
                drawBoardStamps(layout, stamps, stampDrawables)
            }
        }
    }
}
