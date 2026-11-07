package com.cheesecomer.rewardstamp.model

import kotlin.random.Random

private const val MIN_TERM = 11
private const val MAX_TERM = 49

// An addition of two 2-digit numbers that young children cannot solve, but adults can at a glance
data class UnlockProblem(
    val left: Int,
    val right: Int,
) {
    val answer: Int
        get() = left + right

    companion object {
        fun random(random: Random): UnlockProblem =
            UnlockProblem(
                left = random.nextInt(MIN_TERM, MAX_TERM + 1),
                right = random.nextInt(MIN_TERM, MAX_TERM + 1),
            )
    }
}
