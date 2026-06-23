package com.cheesecomer.rewardstamp.model

import android.graphics.drawable.Drawable
import java.time.LocalDateTime

data class GoalStamp(
    val drawable: Drawable,
    val stampedAt: LocalDateTime,
)
