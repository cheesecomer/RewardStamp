package com.cheesecomer.rewardstamp.ui.component.lock

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.UnlockProblem
import com.cheesecomer.rewardstamp.ui.component.dialog.DialogButtons
import com.cheesecomer.rewardstamp.ui.theme.Nikumaru
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import com.cheesecomer.rewardstamp.ui.theme.SheetBackground
import com.cheesecomer.rewardstamp.ui.theme.SheetBorder
import com.cheesecomer.rewardstamp.ui.theme.SheetCard
import com.cheesecomer.rewardstamp.ui.theme.SheetDivider
import com.cheesecomer.rewardstamp.ui.theme.SheetPrimary
import com.cheesecomer.rewardstamp.ui.theme.SheetPrimaryContainer
import kotlinx.coroutines.launch

private const val MAX_ANSWER_LENGTH = 3
private const val SHAKE_STEP_MILLIS = 50

// Swings left and right, a little less each time, like shaking the head
private val shakeOffsets = listOf(12f, -12f, 6f, -6f)
private val shakeSpec =
    keyframes {
        durationMillis = SHAKE_STEP_MILLIS * shakeOffsets.size * 2
        shakeOffsets.forEachIndexed { index, offset ->
            offset at SHAKE_STEP_MILLIS * (index * 2 + 1)
        }
    }
private const val KEY_ROWS = 3

@Composable
private fun LockIcon(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(SheetPrimaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Lock,
            contentDescription = null,
            tint = SheetPrimary,
            modifier = Modifier.size(28.dp),
        )
    }
}

@Composable
private fun UnlockDialogHeader(
    problem: UnlockProblem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        LockIcon()
        Text(
            text = stringResource(R.string.adult_lock_title),
            style = MaterialTheme.typography.titleLarge.copy(fontFamily = Nikumaru),
        )
        Text(
            text = stringResource(R.string.adult_lock_message),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
        )
        HorizontalDivider(color = SheetDivider)
        Text(
            text = stringResource(R.string.adult_lock_problem, problem.left, problem.right),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag(AdultLockDialogTestTags.PROBLEM),
        )
    }
}

@Composable
private fun AnswerField(
    input: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag(AdultLockDialogTestTags.INPUT),
        shape = RoundedCornerShape(16.dp),
        color = SheetCard,
        border = BorderStroke(1.dp, SheetBorder),
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (input.isEmpty()) {
                Text(
                    text = stringResource(R.string.adult_lock_input_placeholder),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                )
            } else {
                Text(
                    text = input,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun Key(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(12.dp),
        color = SheetBackground,
    ) {
        Box(contentAlignment = Alignment.Center) {
            content()
        }
    }
}

@Composable
private fun NumberPad(
    onDigitClick: (Int) -> Unit,
    onClearClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(KEY_ROWS) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(KEY_ROWS) { column ->
                    val digit = row * KEY_ROWS + column + 1
                    Key(
                        onClick = { onDigitClick(digit) },
                        modifier = Modifier.weight(1f).testTag(AdultLockDialogTestTags.digit(digit)),
                    ) {
                        Text(text = digit.toString(), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Key(
                onClick = onClearClick,
                modifier = Modifier.weight(1f).testTag(AdultLockDialogTestTags.CLEAR),
            ) {
                Text(text = stringResource(R.string.adult_lock_clear), fontWeight = FontWeight.Bold)
            }
            Key(
                onClick = { onDigitClick(0) },
                modifier = Modifier.weight(1f).testTag(AdultLockDialogTestTags.digit(0)),
            ) {
                Text(text = "0", fontWeight = FontWeight.Bold)
            }
            Key(
                onClick = onBackspaceClick,
                modifier = Modifier.weight(1f).testTag(AdultLockDialogTestTags.BACKSPACE),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Backspace,
                    contentDescription = stringResource(R.string.adult_lock_backspace),
                )
            }
        }
    }
}

/**
 * Asks for the answer of [problem] with its own number pad, so that no keyboard covers the dialog.
 * When [onAnswer] returns false, the answer is cleared and the field shakes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnlockDialog(
    problem: UnlockProblem,
    onAnswer: (Int) -> Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var input by rememberSaveable(problem) { mutableStateOf("") }
    var wrong by rememberSaveable(problem) { mutableStateOf(false) }
    val shake = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Wider than the default dialog, so that the number pad and the buttons have room
    BasicAlertDialog(
        modifier = modifier.padding(horizontal = 24.dp).testTag(AdultLockDialogTestTags.ROOT),
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = SheetCard,
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                UnlockDialogHeader(problem = problem)
                AnswerField(
                    input = input,
                    modifier = Modifier.offset { IntOffset(shake.value.dp.roundToPx(), 0) },
                )
                if (wrong) {
                    Text(
                        text = stringResource(R.string.adult_lock_wrong_answer),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.testTag(AdultLockDialogTestTags.WRONG_ANSWER),
                    )
                }
                NumberPad(
                    onDigitClick = { digit ->
                        if (input.length < MAX_ANSWER_LENGTH) input += digit
                    },
                    onClearClick = { input = "" },
                    onBackspaceClick = { input = input.dropLast(1) },
                )
                DialogButtons(
                    confirmText = stringResource(R.string.adult_lock_unlock),
                    enable = input.isNotEmpty(),
                    onDismissClick = onDismissRequest,
                    onConfirmClick = {
                        if (!onAnswer(input.toInt())) {
                            input = ""
                            wrong = true
                            scope.launch { shake.animateTo(0f, shakeSpec) }
                        }
                    },
                )
            }
        }
    }
}

// Shows the unlock dialog while [state] waits for an answer
@Composable
fun AdultLockDialog(state: AdultLockState) {
    val problem = state.problem ?: return
    UnlockDialog(
        problem = problem,
        onAnswer = state::answer,
        onDismissRequest = state::cancel,
    )
}

@ExcludeFromCoverage
@Preview
@Composable
private fun UnlockDialogPreview() {
    RewardStampTheme {
        UnlockDialog(
            problem = UnlockProblem(left = 38, right = 47),
            onAnswer = { false },
            onDismissRequest = {},
        )
    }
}
