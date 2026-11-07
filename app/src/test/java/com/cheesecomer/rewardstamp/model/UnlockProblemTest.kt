package com.cheesecomer.rewardstamp.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.random.Random

class UnlockProblemTest {
    @Test
    fun answer_isSum() {
        assertThat(UnlockProblem(left = 38, right = 47).answer).isEqualTo(85)
    }

    @Test
    fun random_addsTwoDigitNumbers() {
        val random = Random(0)

        repeat(100) {
            val problem = UnlockProblem.random(random)

            assertThat(problem.left).isIn(11..49)
            assertThat(problem.right).isIn(11..49)
        }
    }

    @Test
    fun random_isSameForSameSeed() {
        assertThat(UnlockProblem.random(Random(3))).isEqualTo(UnlockProblem.random(Random(3)))
    }
}
