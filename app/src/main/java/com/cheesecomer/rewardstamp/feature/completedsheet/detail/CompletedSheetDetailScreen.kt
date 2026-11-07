package com.cheesecomer.rewardstamp.feature.completedsheet.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.CompletedRewardSheet
import com.cheesecomer.rewardstamp.model.GoalStamp
import com.cheesecomer.rewardstamp.model.GoalStampType
import com.cheesecomer.rewardstamp.model.LockTarget
import com.cheesecomer.rewardstamp.model.RewardStamp
import com.cheesecomer.rewardstamp.model.StampType
import com.cheesecomer.rewardstamp.ui.component.RewardBoardState
import com.cheesecomer.rewardstamp.ui.component.RewardBoardView
import com.cheesecomer.rewardstamp.ui.component.RewardStampScaffold
import com.cheesecomer.rewardstamp.ui.component.dialog.DeleteSheetDialog
import com.cheesecomer.rewardstamp.ui.component.lock.AdultLockDialog
import com.cheesecomer.rewardstamp.ui.component.lock.AdultLockState
import com.cheesecomer.rewardstamp.ui.component.lock.rememberAdultLockState
import com.cheesecomer.rewardstamp.ui.component.lock.rememberAppAdultLockState
import com.cheesecomer.rewardstamp.ui.rememberDrawable
import com.cheesecomer.rewardstamp.ui.rewardStampViewModel
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import java.time.LocalDateTime

@Composable
private fun TitleLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = text,
            style =
                MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            modifier = Modifier.alignByBaseline().testTag(CompletedSheetDetailTestTags.TITLE_LABEL_TEXT),
        )
        Text(
            text = stringResource(R.string.completed_detail_title_suffix),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.alignByBaseline(),
        )
    }
}

@Composable
private fun GoalCountLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.alignByBaseline().testTag(CompletedSheetDetailTestTags.GOAL_COUNT_LABEL_TEXT),
        )
        Text(
            text = stringResource(R.string.completed_detail_goal_count_suffix),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.alignByBaseline(),
        )
    }
}

@Composable
private fun DeleteAction(
    adultLock: AdultLockState,
    onDeleteClick: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    IconButton(
        onClick = { adultLock.runUnlocked(LockTarget.ParentAction) { showDialog = true } },
        modifier = Modifier.testTag(CompletedSheetDetailTestTags.DELETE_BUTTON),
    ) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = stringResource(R.string.common_delete),
        )
    }

    if (showDialog) {
        DeleteSheetDialog(
            title = stringResource(R.string.delete_completed_sheet_title),
            message = stringResource(R.string.delete_completed_sheet_message),
            onDeleteRequest = {
                showDialog = false
                onDeleteClick()
            },
            onDismissRequest = {
                showDialog = false
            },
        )
    }
}

@Composable
internal fun CompletedSheetDetailContent(
    sheet: CompletedRewardSheet?,
    stamps: List<RewardStamp>,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {},
    adultLock: AdultLockState = rememberAdultLockState(),
) {
    if (sheet == null) {
        Text(stringResource(R.string.common_not_found))
        return
    }

    val goalStampDrawable = rememberDrawable(sheet.goalStampType.iconRes)
    AdultLockDialog(adultLock)

    RewardStampScaffold(
        title = stringResource(R.string.completed_title),
        modifier = modifier,
        actions = {
            DeleteAction(adultLock = adultLock, onDeleteClick = onDeleteClick)
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TitleLabel(
                    text = sheet.title,
                    modifier = Modifier.testTag(CompletedSheetDetailTestTags.TITLE_LABEL),
                )
                GoalCountLabel(
                    text = sheet.goalCount.toString(),
                    modifier = Modifier.testTag(CompletedSheetDetailTestTags.GOAL_COUNT_LABEL),
                )
            }

            RewardBoardView(
                board =
                    RewardBoardState(
                        title = sheet.title,
                        currentCount = sheet.goalCount,
                        goalCount = sheet.goalCount,
                    ),
                stamps = stamps,
                goalStamp =
                    GoalStamp(
                        drawable = goalStampDrawable,
                        stampedAt = sheet.completedAt,
                    ),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@ExcludeFromCoverage
@Composable
fun CompletedSheetDetailScreen(
    completedSheetId: Long,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {},
    viewModel: CompletedSheetDetailViewModel =
        rewardStampViewModel {
            CompletedSheetDetailViewModel(
                completedRewardSheetRepository,
                rewardStampRepository,
            )
        },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(completedSheetId) {
        viewModel.load(completedSheetId)
    }

    CompletedSheetDetailContent(
        sheet = uiState.sheet,
        stamps = uiState.stamps,
        modifier = modifier,
        onDeleteClick = {
            viewModel.delete(completedSheetId) {
                onDeleteClick()
            }
        },
        adultLock = rememberAppAdultLockState(),
    )
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun CompletedSheetDetailScreenPreview() {
    val rewardStamp: (Int) -> RewardStamp = @ExcludeFromCoverage { position ->
        RewardStamp(
            id = position.toLong(),
            sheetId = 1,
            completedRewardSheetId = null,
            position = position,
            stampedAt = LocalDateTime.now().minusDays(10 - position.toLong()),
            stampType = StampType.entries.random(),
        )
    }
    RewardStampTheme {
        CompletedSheetDetailContent(
            sheet =
                CompletedRewardSheet(
                    id = 1L,
                    sheetId = 1L,
                    title = "おかたずけ",
                    goalCount = 10,
                    goalStampType = GoalStampType.Bear9,
                    completedAt = LocalDateTime.now(),
                    consumedAt = null,
                ),
            stamps = (0..9).map { rewardStamp(it) },
        )
    }
}
