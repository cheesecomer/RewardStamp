package com.cheesecomer.rewardseal.ui.component
import com.cheesecomer.rewardseal.ui.component.RewardBoardLayout.EMPTY_CELL

val boardPatterns: Map<Int, List<List<Int>>> =
    mapOf(
        1 to
            listOf(
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
                listOf(EMPTY_CELL, 0, EMPTY_CELL),
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
                listOf(EMPTY_CELL, 1, EMPTY_CELL),
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
            ),
        2 to
            listOf(
                listOf(EMPTY_CELL, 0, EMPTY_CELL),
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
                listOf(EMPTY_CELL, 1, EMPTY_CELL),
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
                listOf(EMPTY_CELL, 2, EMPTY_CELL),
            ),
        3 to
            listOf(
                listOf(EMPTY_CELL, 0, EMPTY_CELL),
                listOf(EMPTY_CELL, 1, EMPTY_CELL),
                listOf(EMPTY_CELL, 2, EMPTY_CELL),
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
                listOf(EMPTY_CELL, 3, EMPTY_CELL),
            ),
        4 to
            listOf(
                listOf(EMPTY_CELL, 0, EMPTY_CELL),
                listOf(EMPTY_CELL, 1, EMPTY_CELL),
                listOf(EMPTY_CELL, 2, EMPTY_CELL),
                listOf(EMPTY_CELL, 3, EMPTY_CELL),
                listOf(EMPTY_CELL, 4, EMPTY_CELL),
            ),
        5 to
            listOf(
                listOf(0, EMPTY_CELL, 1),
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
                listOf(3, EMPTY_CELL, 2),
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
                listOf(4, EMPTY_CELL, 5),
            ),
        6 to
            listOf(
                listOf(0, 1, 2),
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
                listOf(4, EMPTY_CELL, 3),
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
                listOf(5, EMPTY_CELL, 6),
            ),
        7 to
            listOf(
                listOf(0, 1, 2),
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
                listOf(5, 4, 3),
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
                listOf(6, EMPTY_CELL, 7),
            ),
        8 to
            listOf(
                listOf(0, 1, 2),
                listOf(EMPTY_CELL, EMPTY_CELL, EMPTY_CELL),
                listOf(5, 4, 3),
                listOf(6, EMPTY_CELL, EMPTY_CELL),
                listOf(7, EMPTY_CELL, 8),
            ),
        9 to
            listOf(
                listOf(0, 1, 2),
                listOf(EMPTY_CELL, EMPTY_CELL, 3),
                listOf(6, 5, 4),
                listOf(7, EMPTY_CELL, EMPTY_CELL),
                listOf(8, EMPTY_CELL, 9),
            ),
        10 to
            listOf(
                listOf(0, 1, 2),
                listOf(4, EMPTY_CELL, 3),
                listOf(5, EMPTY_CELL, 6),
                listOf(8, EMPTY_CELL, 7),
                listOf(9, EMPTY_CELL, 10),
            ),
        11 to
            listOf(
                listOf(0, 1, 2),
                listOf(4, EMPTY_CELL, 3),
                listOf(5, EMPTY_CELL, 6),
                listOf(9, 8, 7),
                listOf(10, EMPTY_CELL, 11),
            ),
        12 to
            listOf(
                listOf(0, 1, 2),
                listOf(4, EMPTY_CELL, 3),
                listOf(5, 6, 7),
                listOf(10, 9, 8),
                listOf(11, EMPTY_CELL, 12),
            ),
        13 to
            listOf(
                listOf(0, 1, 2),
                listOf(5, 4, 3),
                listOf(6, 7, 8),
                listOf(11, 10, 9),
                listOf(12, EMPTY_CELL, 13),
            ),
        14 to
            listOf(
                listOf(0, 1, 2),
                listOf(5, 4, 3),
                listOf(6, 7, 8),
                listOf(11, 10, 9),
                listOf(12, EMPTY_CELL, EMPTY_CELL),
                listOf(13, EMPTY_CELL, 14),
            ),
        15 to
            listOf(
                listOf(0, 1, 2),
                listOf(4, EMPTY_CELL, 3),
                listOf(5, EMPTY_CELL, 6),
                listOf(8, EMPTY_CELL, 7),
                listOf(9, EMPTY_CELL, 10),
                listOf(13, 12, 11),
                listOf(14, EMPTY_CELL, 15),
            ),
        16 to
            listOf(
                listOf(0, 1, 2),
                listOf(4, EMPTY_CELL, 3),
                listOf(5, 6, 7),
                listOf(9, EMPTY_CELL, 8),
                listOf(10, EMPTY_CELL, 11),
                listOf(14, 13, 12),
                listOf(15, EMPTY_CELL, 16),
            ),
        17 to
            listOf(
                listOf(0, 1, 2),
                listOf(4, EMPTY_CELL, 3),
                listOf(5, 6, 7),
                listOf(10, 9, 8),
                listOf(11, EMPTY_CELL, 12),
                listOf(15, 14, 13),
                listOf(16, EMPTY_CELL, 17),
            ),
        18 to
            listOf(
                listOf(0, 1, 2),
                listOf(5, 4, 3),
                listOf(6, 7, 8),
                listOf(11, 10, 9),
                listOf(12, EMPTY_CELL, 13),
                listOf(16, 15, 14),
                listOf(17, EMPTY_CELL, 18),
            ),
        19 to
            listOf(
                listOf(0, 1, 2),
                listOf(5, 4, 3),
                listOf(6, 7, 8),
                listOf(11, 10, 9),
                listOf(12, EMPTY_CELL, 13),
                listOf(16, 15, 14),
                listOf(17, EMPTY_CELL, 18),
            ),
        20 to
            listOf(
                listOf(0, 1, 2),
                listOf(5, 4, 3),
                listOf(6, 7, 8),
                listOf(11, 10, 9),
                listOf(12, 13, 14),
                listOf(17, 16, 15),
                listOf(18, EMPTY_CELL, EMPTY_CELL),
                listOf(19, EMPTY_CELL, 20),
            ),
    )
