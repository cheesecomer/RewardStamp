package com.cheesecomer.rewardstamp.feature.sheet.detail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.cheesecomer.rewardstamp.model.GoalStampType
import com.cheesecomer.rewardstamp.ui.component.StampPress
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.coroutineContext

private const val PRESS_DURATION_MILLIS = 450
private const val PRESS_LAND_MILLIS = 300
private const val PRESS_BOUNCE_MILLIS = 360
private const val PRESS_BOUNCE_PROGRESS = 1.03f

private const val GOAL_STAMP_TIMEOUT_MILLIS = 3_000L
private const val BEFORE_SHOWCASE_MILLIS = 200L
private const val SHOWCASE_MILLIS = 1_500L
private const val AFTER_SHOWCASE_MILLIS = 200L
private const val LINGER_MILLIS = 1_000L

// Lands at PRESS_LAND_MILLIS, then bounces a little
private val pressSpec =
    keyframes {
        durationMillis = PRESS_DURATION_MILLIS
        1f at PRESS_LAND_MILLIS using FastOutLinearInEasing
        PRESS_BOUNCE_PROGRESS at PRESS_BOUNCE_MILLIS
    }

enum class GoalCelebrationPhase {
    None,
    Showcase,
    GoalPress,
    Linger,
}

/**
 * Plays the press of the latest stamp. When the stamp reaches the goal, it goes on to show the goal stamp
 * in the middle of the screen with confetti, press it onto the goal, and wait a little before the sheet is finished.
 * Without [showcases], the goal stamp is put on right after the press.
 */
@Stable
class StampAnimationState(
    pressed: Boolean,
    val celebrates: Boolean,
    private val showcases: Boolean = true,
) {
    private val pressProgress = Animatable(if (pressed) 0f else 1f)
    private val goalPressProgress = Animatable(if (celebrates) 0f else 1f)

    fun press(position: Int?): StampPress =
        StampPress(
            position = position,
            progress = { pressProgress.value },
            goalProgress = { goalPressProgress.value },
        )

    var phase by mutableStateOf(GoalCelebrationPhase.None)
        private set

    var isFinished by mutableStateOf(!celebrates)
        private set

    var isSkipped by mutableStateOf(false)
        private set

    fun skip() {
        isSkipped = true
    }

    internal suspend fun play(
        goalStampType: suspend () -> GoalStampType,
        onLand: () -> Unit,
    ) {
        if (pressProgress.value < 1f) {
            pressProgress.animateTo(1f, pressSpec)
            onLand()
        }
        if (isFinished) return

        if (!showcases || withTimeoutOrNull(GOAL_STAMP_TIMEOUT_MILLIS) { goalStampType() } == null) {
            finish()
            return
        }

        scaledDelay(BEFORE_SHOWCASE_MILLIS)
        phase = GoalCelebrationPhase.Showcase
        scaledDelay(SHOWCASE_MILLIS)

        phase = GoalCelebrationPhase.GoalPress
        scaledDelay(AFTER_SHOWCASE_MILLIS)
        goalPressProgress.animateTo(1f, pressSpec)
        onLand()

        phase = GoalCelebrationPhase.Linger
        scaledDelay(LINGER_MILLIS)
        finish()
    }

    internal suspend fun finish() {
        pressProgress.snapTo(1f)
        goalPressProgress.snapTo(1f)
        phase = GoalCelebrationPhase.None
        isFinished = true
    }
}

// Follows the animator duration scale of the system like the animations do, so turning animations off skips the waits
private suspend fun scaledDelay(millis: Long) {
    val scale = coroutineContext[MotionDurationScale]?.scaleFactor ?: 1f
    delay((millis * scale).toLong())
}

@Composable
fun rememberStampAnimationState(
    stampedPosition: Int?,
    goalCount: Int,
    goalStampType: GoalStampType?,
    showcasesGoal: Boolean = true,
): StampAnimationState {
    val state =
        remember(stampedPosition) {
            StampAnimationState(
                pressed = stampedPosition != null,
                celebrates = stampedPosition == goalCount - 1,
                showcases = showcasesGoal,
            )
        }
    val currentGoalStampType by rememberUpdatedState(goalStampType)
    val hapticFeedback = LocalHapticFeedback.current

    LaunchedEffect(state, state.isSkipped) {
        if (state.isSkipped) {
            state.finish()
        } else {
            state.play(
                goalStampType = {
                    snapshotFlow { currentGoalStampType }.filterNotNull().first()
                },
                onLand = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                },
            )
        }
    }

    return state
}
