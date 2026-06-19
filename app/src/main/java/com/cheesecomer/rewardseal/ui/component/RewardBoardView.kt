package com.cheesecomer.rewardseal.ui.component

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.withSave
import com.cheesecomer.rewardseal.R
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.model.RewardStamp
import com.cheesecomer.rewardseal.model.StampType
import com.cheesecomer.rewardseal.ui.component.RewardBoardLayout.EMPTY_CELL
import com.cheesecomer.rewardseal.ui.component.RewardBoardLayout.GOAL_CELL_SCALE
import com.cheesecomer.rewardseal.ui.component.RewardBoardLayout.STAMP_OFFSET_RANGE
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import com.cheesecomer.rewardseal.ui.theme.SheetText
import com.cheesecomer.rewardseal.ui.theme.StampInk
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Composable
fun RewardBoardView(
    board: RewardBoardState,
    stamps: List<RewardStamp>,
    modifier: Modifier = Modifier,
) {
    val pattern = boardPatterns[board.goalCount]
    if (pattern == null) {
        Text("Unsupported goal count: ${board.goalCount}")
        return
    }

    val scrollState = rememberScrollState()
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
    ) {
        val metrics =
            BoardLayoutMetrics.build(
                containerHeight = this.maxHeight,
                containerWidth = this.maxWidth,
                rowNum = pattern.size,
            )

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
        ) {
            RewardBoardCanvas(
                metrics = metrics,
                pattern = pattern,
                stamps = stamps,
                board = board,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(metrics.canvasHeight),
            )
        }
    }
}

data class RewardBoardState(
    val title: String,
    val currentCount: Int,
    val goalCount: Int,
)

private fun buildBoardPoints(
    pattern: List<List<Int>>,
    metrics: BoardLayoutMetrics,
    density: Density,
    cellNum: Int,
    size: Size,
): Map<Int, Offset> {
    val columnXs =
        listOf(
            size.width * RewardBoardLayout.FIRST_CELL_RATIO,
            size.width * RewardBoardLayout.SECOND_CELL_RATIO,
            size.width * RewardBoardLayout.THIRD_CELL_RATIO,
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
                        size.height - ((rowHeight * GOAL_CELL_SCALE) / 2f)
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

private fun stampOffset(stamp: RewardStamp): Offset {
    val random = Random(stamp.randomSeed)

    return Offset(
        random.nextFloat() * (STAMP_OFFSET_RANGE * 2) - STAMP_OFFSET_RANGE,
        random.nextFloat() * (STAMP_OFFSET_RANGE * 2) - STAMP_OFFSET_RANGE,
    )
}

private fun Canvas.drawStampIcon(
    drawable: Drawable,
    center: Offset,
    offset: Offset,
    iconSizePx: Float,
) {
    val left = (center.x - iconSizePx / 2f + offset.x).toInt()
    val top = (center.y - iconSizePx / 2f + offset.y).toInt()
    val right = (left + iconSizePx).toInt()
    val bottom = (top + iconSizePx).toInt()
    drawable.setBounds(
        left,
        top,
        right,
        bottom,
    )

    drawable.setTint(StampInk.copy(alpha = 0.9f).toArgb())
    drawable.draw(this)
}

private fun Canvas.drawStampStampedAt(
    stampedAt: LocalDateTime,
    textSizePx: Float,
    center: Offset,
) {
    val dateText =
        stampedAt.format(
            DateTimeFormatter.ofPattern("M/d"),
        )

    val textPaint =
        android.graphics.Paint().apply {
            isAntiAlias = true
            color = SheetText.toArgb()
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = textSizePx
            typeface =
                android.graphics.Typeface.create(
                    android.graphics.Typeface.DEFAULT,
                    android.graphics.Typeface.BOLD,
                )
            color = StampInk.copy(alpha = 0.9f).toArgb()
        }

    drawText(
        dateText,
        center.x,
        center.y,
        textPaint,
    )
}

private fun DrawScope.drawStamps(
    pointsByIndex: Map<Int, Offset>,
    metrics: BoardLayoutMetrics,
    stamps: List<RewardStamp>,
    stampDrawables: Map<StampType, Drawable>,
) {
    val stampRadius = metrics.cellRadius.toPx()
    val iconSizePx = metrics.iconSize.toPx()
    stamps.forEach { stamp ->
        val center = pointsByIndex[stamp.position]
        if (center == null) return

        val drawable = stampDrawables[stamp.stampType]
        if (drawable == null) return

        val offset = stampOffset(stamp)
        val rotation = Random(stamp.randomSeed).nextFloat() * 20f - 10f

        drawIntoCanvas { canvas ->
            val nativeCanvas = canvas.nativeCanvas

            nativeCanvas.withSave {
                rotate(
                    rotation,
                    center.x,
                    center.y,
                )

                drawStampIcon(
                    drawable,
                    center = center,
                    offset = offset,
                    iconSizePx = iconSizePx,
                )

                drawStampStampedAt(
                    stamp.stampedAt,
                    15.sp.toPx(),
                    center + offset + Offset(0f, stampRadius - 4.dp.toPx()),
                )
            }
            nativeCanvas.drawStampScratches(
                center = center,
                radius = stampRadius,
                seed = stamp.randomSeed,
                backgroundColor = Color.White.copy(alpha = 0.2f),
            )
        }
    }
}

private const val LINE_SCRATCH_NUM = 10
private const val DOT_SCRATCH_NUM = 18

private const val LINE_SCRATCH_WIDTH_MIN_RATIO = 0.01f
private const val LINE_SCRATCH_WIDTH_MAX_RATIO = 0.05f

private fun Canvas.drawStampScratches(
    center: Offset,
    radius: Float,
    seed: Long,
    backgroundColor: Color,
) {
    val random = Random(seed)
    val paint =
        android.graphics.Paint().apply {
            isAntiAlias = true
            strokeCap = android.graphics.Paint.Cap.ROUND
        }
    repeat(LINE_SCRATCH_NUM) {
        paint.color = backgroundColor.copy(alpha = (backgroundColor.alpha * random.nextFloat())).toArgb()
        paint.strokeWidth = radius * (LINE_SCRATCH_WIDTH_MIN_RATIO + random.nextFloat() * LINE_SCRATCH_WIDTH_MAX_RATIO)
        val y1 = center.y + random.nextFloat() * radius * 1.25f - radius * 0.7f
        val x1 = center.x + random.nextFloat() * radius * 1.25f - radius * 0.7f
        val y2 = y1 + random.nextFloat() * radius * 0.15f - radius * 0.075f
        val x2 = x1 + random.nextFloat() * radius * 0.25f + radius * 0.15f

        drawLine(
            x1,
            y1,
            x2,
            y2,
            paint,
        )
    }

    repeat(DOT_SCRATCH_NUM) {
        paint.color = backgroundColor.copy(alpha = (backgroundColor.alpha * random.nextFloat())).toArgb()
        val x = center.x + random.nextFloat() * radius * 1.4f - radius * 0.7f
        val y = center.y + random.nextFloat() * radius * 1.4f - radius * 0.7f
        val dotRadius = random.nextFloat() * radius * 0.035f + radius * 0.015f

        drawCircle(x, y, dotRadius, paint)
    }
}

@Composable
private fun RewardBoardCanvas(
    metrics: BoardLayoutMetrics,
    pattern: List<List<Int>>,
    board: RewardBoardState,
    stamps: List<RewardStamp>,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val context = LocalContext.current
    val stampDrawables =
        remember {
            StampType.entries.associateWith { stampType ->
                ContextCompat.getDrawable(context, stampType.iconRes)!!
            }
        }
    Canvas(
        modifier =
            modifier
                .fillMaxWidth()
                .height(metrics.canvasHeight),
    ) {
        val pointsByIndex =
            buildBoardPoints(
                pattern = pattern,
                metrics = metrics,
                density = density,
                size = size,
                cellNum = board.goalCount,
            )
        drawLines(
            pointsByIndex = pointsByIndex,
            currentCount = board.currentCount,
        )

        drawCells(
            pointsByIndex = pointsByIndex,
            metrics = metrics,
            board = board,
        )

        val goalOffset = pointsByIndex[board.goalCount]
        if (goalOffset != null) {
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_crown)!!
            drawGoalMark(
                center = goalOffset,
                metrics = metrics,
                drawable = drawable,
            )
        }

        drawStamps(
            pointsByIndex = pointsByIndex,
            stamps = stamps,
            metrics = metrics,
            stampDrawables = stampDrawables,
        )
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun RewardBoardViewPreview() {
    val rewardStamp: (Int) -> RewardStamp =
        @ExcludeFromCoverage { position ->
            RewardStamp(
                id = position.toLong(),
                sheetId = 1,
                completedRewardSheetId = null,
                position = position,
                stampedAt = LocalDateTime.now().minusSeconds(10 - position.toLong()),
                stampType = StampType.Star,
            )
        }
    RewardSealTheme {
        RewardBoardView(
            board =
                RewardBoardState(
                    title = "おてつだい",
                    currentCount = 6,
                    goalCount = 10,
                ),
            stamps =
                listOf(
                    rewardStamp(0),
                    rewardStamp(1),
                    rewardStamp(2),
                    rewardStamp(3),
                    rewardStamp(4),
                    rewardStamp(5),
                ),
        )
    }
}
