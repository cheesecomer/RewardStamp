package com.cheesecomer.rewardseal.ui.component

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.cheesecomer.rewardseal.data.rewardStamp
import com.cheesecomer.rewardseal.model.StampType
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class RewardBoardViewTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL,
            theme = "android:style/Theme.Material.Light.NoActionBar",
        )

    @Test
    fun rewardBoardView() {
        val now = LocalDateTime.parse("2026-06-01T00:00:00")
        paparazzi.snapshot {
            RewardSealTheme {
                RewardBoardView(
                    modifier = Modifier.testTag("RewardBoard"),
                    board =
                        RewardBoardState(
                            title = "おてつだい",
                            currentCount = 0,
                            goalCount = 10,
                        ),
                    stamps =
                        listOf(
                            rewardStamp(1, position = 0, stampedAt = now.plusDays(0L), stampType = StampType.Star),
                            rewardStamp(2, position = 1, stampedAt = now.plusDays(1L), stampType = StampType.Butterfly),
                            rewardStamp(3, position = 2, stampedAt = now.plusDays(2L), stampType = StampType.Frog),
                            rewardStamp(4, position = 3, stampedAt = now.plusDays(3L), stampType = StampType.CherryBlossom),
                            rewardStamp(5, position = 4, stampedAt = now.plusDays(4L), stampType = StampType.Dog),
                            rewardStamp(6, position = 5, stampedAt = now.plusDays(5L), stampType = StampType.Bear2),
                            rewardStamp(7, position = 6, stampedAt = now.plusDays(6L), stampType = StampType.Rabbit),
                            rewardStamp(8, position = 7, stampedAt = now.plusDays(7L), stampType = StampType.Apple),
                            rewardStamp(9, position = 8, stampedAt = now.plusDays(8L), stampType = StampType.Paw),
                        ),
                )
            }
        }
    }
}
