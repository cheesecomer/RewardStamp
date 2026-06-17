package com.cheesecomer.rewardseal.ui.component

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RewardBoardLayoutTest {
    @Test
    fun create_calculatesLayoutValues() {
        val boardSize =
            RewardBoardSize.create(
                maxHeight = 1300.dp,
                goalCount = 10,
            )

        val layout =
            RewardBoardLayout.create(
                canvasSize =
                    Size(
                        width = 500f,
                        height = 1300f,
                    ),
                boardSize = boardSize,
            )
        Truth.assertThat(layout.cellHeightPx).isWithin(0.001f).of(100f)
        Truth.assertThat(layout.topMargin).isWithin(0.001f).of(100f)
        Truth.assertThat(layout.bottomMargin).isWithin(0.001f).of(200f)
        Truth.assertThat(layout.nodeRadius).isWithin(0.001f).of(22f)
        Truth.assertThat(layout.goalRadius).isWithin(0.001f).of(34f)
        Truth.assertThat(layout.strokeWidth).isWithin(0.001f).of(6f)
        Truth.assertThat(layout.leftMargin).isWithin(0.001f).of(30f)
        Truth.assertThat(layout.rightMargin).isWithin(0.001f).of(30f)
        Truth.assertThat(layout.drawableWidth).isWithin(0.001f).of(440f)
        Truth.assertThat(layout.drawableHeight).isWithin(0.001f).of(1000f)
        Truth.assertThat(layout.centerX).isWithin(0.001f).of(250f)
        Truth.assertThat(layout.amplitude).isWithin(0.001f).of(158.4f)
    }

    @Test
    fun create_placesFirstPointAtTopMargin() {
        val boardSize =
            RewardBoardSize.create(
                maxHeight = 1300.dp,
                goalCount = 10,
            )

        val layout =
            RewardBoardLayout.create(
                canvasSize =
                    Size(
                        width = 500f,
                        height = 1300f,
                    ),
                boardSize = boardSize,
            )

        Truth.assertThat(layout.points.first().y).isEqualTo(layout.topMargin)
    }

    @Test
    fun create_placesLastPointAtDrawableBottom() {
        val boardSize =
            RewardBoardSize.create(
                maxHeight = 1300.dp,
                goalCount = 10,
            )

        val layout =
            RewardBoardLayout.create(
                canvasSize =
                    Size(
                        width = 500f,
                        height = 1300f,
                    ),
                boardSize = boardSize,
            )

        Truth
            .assertThat(layout.points.last().y)
            .isWithin(0.001f)
            .of(layout.topMargin + layout.drawableHeight)
    }

    @Test
    fun create_placesPointsAlternatelyLeftAndRight() {
        val boardSize =
            RewardBoardSize.create(
                maxHeight = 1300.dp,
                goalCount = 4,
            )

        val layout =
            RewardBoardLayout.create(
                canvasSize =
                    Size(
                        width = 500f,
                        height = 1300f,
                    ),
                boardSize = boardSize,
            )

        Truth.assertThat(layout.points[0].x).isLessThan(layout.centerX)
        Truth.assertThat(layout.points[1].x).isGreaterThan(layout.centerX)
        Truth.assertThat(layout.points[2].x).isLessThan(layout.centerX)
        Truth.assertThat(layout.points[3].x).isGreaterThan(layout.centerX)
    }

    @Test
    fun create_generatesDeterministicPoints() {
        val boardSize =
            RewardBoardSize.create(
                maxHeight = 1300.dp,
                goalCount = 10,
            )

        val first =
            RewardBoardLayout.create(
                canvasSize =
                    Size(
                        width = 500f,
                        height = 1300f,
                    ),
                boardSize = boardSize,
            )

        val second =
            RewardBoardLayout.create(
                canvasSize =
                    Size(
                        width = 500f,
                        height = 1300f,
                    ),
                boardSize = boardSize,
            )

        Truth.assertThat(second.points).isEqualTo(first.points)
    }
}
