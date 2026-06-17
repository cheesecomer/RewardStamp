package com.cheesecomer.rewardseal.ui.component

import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RewardBoardSizeTest {
    @Test
    fun create_usesVisibleCellsWhenGoalCountIsSmall() {
        val size =
            RewardBoardSize.create(
                maxHeight = 1300.dp,
                goalCount = 10,
            )

        assertThat(size.totalCells).isEqualTo(13)
        assertThat(size.cellHeight).isEqualTo(100.dp)
        assertThat(size.boardHeight).isEqualTo(1300.dp)
    }

    @Test
    fun create_expandsBoardWhenGoalCountIsLarge() {
        val size =
            RewardBoardSize.create(
                maxHeight = 1300.dp,
                goalCount = 20,
            )

        assertThat(size.totalCells).isEqualTo(23)
        assertThat(size.cellHeight).isEqualTo(100.dp)
        assertThat(size.boardHeight).isEqualTo(2300.dp)
    }
}
