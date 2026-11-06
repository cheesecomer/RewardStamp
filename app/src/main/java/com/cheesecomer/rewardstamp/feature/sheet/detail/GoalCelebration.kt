package com.cheesecomer.rewardstamp.feature.sheet.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.model.GoalStampType
import com.cheesecomer.rewardstamp.ui.theme.SheetBackground
import com.cheesecomer.rewardstamp.ui.theme.SheetBorder
import com.cheesecomer.rewardstamp.ui.theme.SheetPrimary
import com.cheesecomer.rewardstamp.ui.theme.StampInk
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.rememberReduceMotion
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter

private const val SHOWCASE_EXIT_SCALE = 1.5f
private const val SCRIM_ALPHA = 0.7f
private const val CRACKER_ANGLE = 60
private const val CRACKER_SPREAD = 30
private const val CRACKER_SPEED = 40f
private const val CRACKER_MAX_SPEED = 80f
private const val CRACKER_TIME_TO_LIVE_MILLIS = 3_000L
private const val CRACKER_EMIT_MILLIS = 200L
private const val CRACKER_PARTICLES = 120

private val crackerColors =
    listOf(
        SheetPrimary,
        SheetBorder,
        StampInk,
        Color(0xFFFFE18A),
        Color(0xFF8ADCFC),
        Color(0xFFB48DEF),
    ).map { it.toArgb() }

// Two crackers popping from the bottom corners toward the middle
private val crackerParties =
    listOf(
        Position.Relative(0.0, 1.0) to Angle.TOP + CRACKER_ANGLE / 2,
        Position.Relative(1.0, 1.0) to Angle.TOP - CRACKER_ANGLE / 2,
    ).map { (position, angle) ->
        Party(
            angle = angle,
            spread = CRACKER_SPREAD,
            speed = CRACKER_SPEED,
            maxSpeed = CRACKER_MAX_SPEED,
            colors = crackerColors,
            timeToLive = CRACKER_TIME_TO_LIVE_MILLIS,
            position = position,
            emitter = Emitter(duration = CRACKER_EMIT_MILLIS).max(CRACKER_PARTICLES),
        )
    }

@Composable
private fun GoalStampShowcase(
    goalStampType: GoalStampType,
    visible: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(SheetBackground.copy(alpha = SCRIM_ALPHA)),
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter =
                scaleIn(
                    spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow,
                    ),
                ) + fadeIn(),
            exit = scaleOut(targetScale = SHOWCASE_EXIT_SCALE) + fadeOut(),
        ) {
            Image(
                painter = painterResource(goalStampType.iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(StampInk),
                modifier =
                    Modifier
                        .size(240.dp)
                        .testTag(GoalCelebrationTestTags.GOAL_STAMP),
            )
        }
    }
}

@Composable
fun GoalCelebration(
    state: StampAnimationState,
    goalStampType: GoalStampType?,
    modifier: Modifier = Modifier,
) {
    if (state.isFinished) return

    val reduceMotion = rememberReduceMotion()
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .testTag(GoalCelebrationTestTags.ROOT)
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClickLabel = stringResource(R.string.goal_celebration_skip),
                    onClick = state::skip,
                ),
        contentAlignment = Alignment.Center,
    ) {
        if (goalStampType != null) {
            GoalStampShowcase(
                goalStampType = goalStampType,
                visible = state.phase == GoalCelebrationPhase.Showcase,
            )
        }

        if (state.phase != GoalCelebrationPhase.None && state.showsConfetti && !reduceMotion) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = crackerParties,
            )
        }
    }
}
