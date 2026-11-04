package com.cheesecomer.rewardstamp.ui.component

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.withSave
import com.cheesecomer.rewardstamp.model.GoalStamp
import com.cheesecomer.rewardstamp.model.RewardStamp
import com.cheesecomer.rewardstamp.model.StampType
import com.cheesecomer.rewardstamp.ui.component.RewardBoardLayout.STAMP_OFFSET_RANGE
import com.cheesecomer.rewardstamp.ui.theme.SheetText
import com.cheesecomer.rewardstamp.ui.theme.StampInk
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.random.Random

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
    val intrinsicWidth = drawable.intrinsicWidth.toFloat()
    val intrinsicHeight = drawable.intrinsicHeight.toFloat()
    val scale = iconSizePx / max(intrinsicWidth, intrinsicHeight)
    val width = intrinsicWidth * scale
    val height = intrinsicHeight * scale
    val left = (center.x - width / 2 + offset.x).toInt()
    val top = (center.y - height / 2 + offset.y).toInt()

    drawable.setBounds(
        left,
        top,
        (left + width).toInt(),
        (top + height).toInt(),
    )

    drawable.setTint(StampInk.copy(alpha = 0.9f).toArgb())
    drawable.draw(this)
}

private fun Canvas.drawStampStampedAt(
    stampedAt: LocalDateTime,
    textSizePx: Float,
    center: Offset,
    format: String = "M/d",
) {
    val dateText =
        stampedAt.format(
            DateTimeFormatter.ofPattern(format),
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

private const val GOAL_STAMP_SCALE = 0.8f

private fun DrawScope.drawGoalStamp(
    pointsByIndex: Map<Int, Offset>,
    metrics: BoardLayoutMetrics,
    goalStampDrawable: Drawable,
    stampedAt: LocalDateTime,
) {
    drawIntoCanvas { canvas ->
        val nativeCanvas = canvas.nativeCanvas
        val center = pointsByIndex[pointsByIndex.size - 1]!!

        nativeCanvas.withSave {
            val iconSizePx =
                arrayOf(
                    (size.width - center.x) * 2f * GOAL_STAMP_SCALE,
                    (size.height - center.y) * 2f * GOAL_STAMP_SCALE,
                    metrics.goalIconMaxSize.toPx(),
                ).min()
            drawStampIcon(
                goalStampDrawable,
                center = center,
                offset = Offset(x = 0f, y = 0f),
                iconSizePx = iconSizePx,
            )

            drawStampStampedAt(
                stampedAt,
                19.sp.toPx(),
                center + Offset(0f, iconSizePx / 2f + 12.dp.toPx()),
                format = "y/M/d",
            )
        }

        nativeCanvas.drawStampScratches(
            center = center,
            radius = 100.dp.toPx(),
            seed = 0L, // FixMe
            backgroundColor = Color.White.copy(alpha = 0.2f),
        )
    }
}

internal fun DrawScope.drawStamps(
    pointsByIndex: Map<Int, Offset>,
    metrics: BoardLayoutMetrics,
    stamps: List<RewardStamp>,
    stampDrawables: Map<StampType, Drawable>,
    goalStamp: GoalStamp?,
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
                    center + offset + Offset(0f, stampRadius + 4.dp.toPx()),
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

    if (goalStamp != null) {
        drawGoalStamp(
            pointsByIndex,
            metrics,
            goalStamp.drawable,
            goalStamp.stampedAt,
        )
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
