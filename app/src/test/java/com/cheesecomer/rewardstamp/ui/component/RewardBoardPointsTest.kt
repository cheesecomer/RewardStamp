package com.cheesecomer.rewardstamp.ui.component

import androidx.compose.ui.geometry.Offset
import com.cheesecomer.rewardstamp.data.rewardStamp
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RewardBoardPointsTest {
    private val pointsByIndex =
        mapOf(
            0 to Offset(100f, 100f),
            1 to Offset(300f, 100f),
        )
    private val stamps =
        listOf(
            rewardStamp(id = 1, position = 0),
            rewardStamp(id = 2, position = 1),
        )

    @Test
    fun findStampAt_returnsStampWhoseCellContainsPoint() {
        val actual = findStampAt(Offset(330f, 120f), pointsByIndex, stamps, cellRadiusPx = 50f)

        assertThat(actual?.id).isEqualTo(2)
    }

    @Test
    fun findStampAt_returnsNullOutsideCells() {
        val actual = findStampAt(Offset(200f, 100f), pointsByIndex, stamps, cellRadiusPx = 50f)

        assertThat(actual).isNull()
    }

    @Test
    fun findStampAt_ignoresStampsWithoutCell() {
        val actual =
            findStampAt(
                point = Offset(100f, 100f),
                pointsByIndex = emptyMap(),
                stamps = stamps,
                cellRadiusPx = 50f,
            )

        assertThat(actual).isNull()
    }
}
