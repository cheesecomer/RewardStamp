package com.cheesecomer.rewardstamp.ui.component

import android.graphics.Canvas
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.GoalStamp
import com.cheesecomer.rewardstamp.model.GoalStampType
import com.cheesecomer.rewardstamp.model.RewardStamp
import com.cheesecomer.rewardstamp.model.StampType
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import java.time.LocalDateTime

@Composable
fun RewardBoardView(
    board: RewardBoardState,
    stamps: List<RewardStamp>,
    modifier: Modifier = Modifier,
    goalStamp: GoalStamp? = null,
    press: StampPress = StampPress(),
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
                goalStamp = goalStamp,
                press = press,
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

@Composable
private fun RewardBoardCanvas(
    metrics: BoardLayoutMetrics,
    pattern: List<List<Int>>,
    board: RewardBoardState,
    stamps: List<RewardStamp>,
    goalStamp: GoalStamp?,
    press: StampPress,
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
                canvasSize = size,
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
            press = press,
        )

        if (goalStamp != null) {
            drawGoalStamp(
                pointsByIndex,
                metrics,
                goalStamp.drawable,
                goalStamp.stampedAt,
                press.goalProgress(),
            )
        }
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
                stampType = StampType.entries.random(),
            )
        }
    val context = LocalContext.current
    val goalStamp =
        GoalStamp(
            drawable = ContextCompat.getDrawable(context, GoalStampType.Bear6.iconRes)!!,
            stampedAt = LocalDateTime.now(),
        )

    RewardStampTheme {
        RewardBoardView(
            board =
                RewardBoardState(
                    title = "おてつだい",
                    currentCount = 10,
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
            goalStamp = goalStamp,
        )
    }
}
