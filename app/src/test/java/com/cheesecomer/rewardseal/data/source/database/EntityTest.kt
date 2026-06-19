package com.cheesecomer.rewardseal.data.source.database

import com.cheesecomer.rewardseal.data.source.database.entity.CompletedRewardSheetEntity
import com.cheesecomer.rewardseal.data.source.database.entity.RewardMilestoneEntity
import com.cheesecomer.rewardseal.data.source.database.entity.RewardSheetEntity
import com.cheesecomer.rewardseal.data.source.database.entity.RewardStampEntity
import com.cheesecomer.rewardseal.model.StampType
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDateTime

class EntityTest {
    @Test
    fun rewardMilestoneEntityHasDefaultId() {
        val entity =
            RewardMilestoneEntity(
                sheetId = 1L,
                requiredCompletions = 1,
                reward = "アイス",
            )

        assertThat(entity.id).isEqualTo(0L)
    }

    @Test
    fun rewardStampEntityHasDefaultId() {
        val entity =
            RewardStampEntity(
                sheetId = 1L,
                completedRewardSheetId = null,
                position = 0,
                stampTypeId = StampType.Hippopotamus.id,
                stampedAt = LocalDateTime.of(2026, 6, 19, 12, 0),
            )

        assertThat(entity.id).isEqualTo(0L)
    }

    @Test
    fun rewardSheetEntityHasDefaultId() {
        val entity =
            RewardSheetEntity(
                title = "はみがき",
                goalCount = 10,
                currentCount = 0,
                deletedAt = null,
            )

        assertThat(entity.id).isEqualTo(0L)
    }

    @Test
    fun completedRewardSheetEntityHasDefaultId() {
        val entity =
            CompletedRewardSheetEntity(
                sheetId = 1L,
                title = "はみがき",
                goalCount = 10,
                completedAt = LocalDateTime.of(2026, 6, 19, 12, 0),
                consumedAt = null,
            )

        assertThat(entity.id).isEqualTo(0L)
    }
}
