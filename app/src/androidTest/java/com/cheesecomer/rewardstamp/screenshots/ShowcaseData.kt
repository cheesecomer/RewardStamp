package com.cheesecomer.rewardstamp.screenshots

import com.cheesecomer.rewardstamp.data.source.database.AppDatabase
import com.cheesecomer.rewardstamp.data.source.database.entity.CompletedRewardSheetEntity
import com.cheesecomer.rewardstamp.data.source.database.entity.RewardMilestoneEntity
import com.cheesecomer.rewardstamp.data.source.database.entity.RewardSheetEntity
import com.cheesecomer.rewardstamp.data.source.database.entity.RewardStampEntity
import com.cheesecomer.rewardstamp.model.GoalStampType
import com.cheesecomer.rewardstamp.model.StampType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * スクリーンショット用のデータ。前のデータは消してから入れる。
 * 日付は撮った日から数えるので、いつ撮っても最近のきろくに見える
 */
internal class ShowcaseData(
    private val database: AppDatabase,
    private val today: LocalDate = LocalDate.now(),
) {
    /** ゴールまであと1回。ツアーでスタンプを押してゴールさせる */
    var toothbrushingId = 0L
        private set

    /** れきしの詳細で開くきろく（一覧の上の方にある） */
    var featuredCompletedSheetId = 0L
        private set

    suspend fun insert() {
        database.clearAllTables()

        toothbrushingId = insertSheet(TOOTHBRUSHING, goalCount = 10, stamps = TOOTHBRUSHING_STAMPS)
        val tidyingId = insertSheet(TIDYING, goalCount = 10, stamps = listOf(StampType.Turtle, StampType.Cat))
        insertSheet(HELPING, goalCount = 15, stamps = emptyList())

        insertMilestone(toothbrushingId, 1, "おかし")
        insertMilestone(toothbrushingId, 2, "おもちゃ")
        insertMilestone(toothbrushingId, 5, "どうぶつえん")
        insertMilestone(tidyingId, 3, "えほん")

        // 古い順。交換済みのものと、まだ交換していないもの（ツアーのゴールと合わせて3枚になる）
        insertCompletedSheet(toothbrushingId, TOOTHBRUSHING, GoalStampType.Bear5, daysAgo = 40, received = true)
        insertCompletedSheet(tidyingId, TIDYING, GoalStampType.CherryBlossom3, daysAgo = 32, received = true)
        insertCompletedSheet(toothbrushingId, TOOTHBRUSHING, GoalStampType.Heart1, daysAgo = 28, received = true)
        insertCompletedSheet(toothbrushingId, TOOTHBRUSHING, GoalStampType.Apple1, daysAgo = 18, received = false)
        featuredCompletedSheetId =
            insertCompletedSheet(toothbrushingId, TOOTHBRUSHING, GoalStampType.Star1, daysAgo = 9, received = false)
    }

    private suspend fun insertSheet(
        title: String,
        goalCount: Int,
        stamps: List<StampType>,
    ): Long {
        val sheetId =
            database.rewardSheetDao().insert(
                RewardSheetEntity(title = title, currentCount = stamps.size, goalCount = goalCount, deletedAt = null),
            )
        insertStamps(sheetId, completedRewardSheetId = null, stamps = stamps, lastDay = today)
        return sheetId
    }

    private suspend fun insertMilestone(
        sheetId: Long,
        requiredSheetCount: Int,
        reward: String,
    ) {
        database.rewardMilestoneDao().insert(
            RewardMilestoneEntity(sheetId = sheetId, requiredSheetCount = requiredSheetCount, reward = reward),
        )
    }

    private suspend fun insertCompletedSheet(
        sheetId: Long,
        title: String,
        goalStampType: GoalStampType,
        daysAgo: Long,
        received: Boolean,
    ): Long {
        val completedAt = today.minusDays(daysAgo).atTime(STAMP_TIME)
        val completedSheetId =
            database.completedRewardSheetDao().insert(
                CompletedRewardSheetEntity(
                    sheetId = sheetId,
                    title = title,
                    goalCount = COMPLETED_GOAL_COUNT,
                    stampTypeId = goalStampType.id,
                    completedAt = completedAt,
                    consumedAt = if (received) completedAt.plusDays(1) else null,
                ),
            )
        val stamps = List(COMPLETED_GOAL_COUNT) { StampType.entries[(it + daysAgo.toInt()) % StampType.entries.size] }
        insertStamps(sheetId, completedSheetId, stamps, lastDay = completedAt.toLocalDate())
        return completedSheetId
    }

    /** 1日1回ずつ押して、最後のスタンプが [lastDay] になるように入れる */
    private suspend fun insertStamps(
        sheetId: Long,
        completedRewardSheetId: Long?,
        stamps: List<StampType>,
        lastDay: LocalDate,
    ) {
        stamps.forEachIndexed { position, stampType ->
            val stampedAt: LocalDateTime = lastDay.minusDays((stamps.size - 1 - position).toLong()).atTime(STAMP_TIME)
            database.rewardStampDao().insert(
                RewardStampEntity(
                    sheetId = sheetId,
                    completedRewardSheetId = completedRewardSheetId,
                    position = position,
                    stampTypeId = stampType.id,
                    stampedAt = stampedAt,
                ),
            )
        }
    }

    companion object {
        const val TOOTHBRUSHING = "はみがき"
        const val TIDYING = "おかたづけ"
        const val HELPING = "おてつだい"

        private const val COMPLETED_GOAL_COUNT = 10
        private val STAMP_TIME: LocalTime = LocalTime.of(19, 30)
        private val TOOTHBRUSHING_STAMPS =
            listOf(
                StampType.Star,
                StampType.Cat,
                StampType.Bear1,
                StampType.Frog,
                StampType.Mammoth,
                StampType.Bear2,
                StampType.Horse,
                StampType.Rabbit,
                StampType.Hippopotamus,
            )
    }
}
