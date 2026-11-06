package com.cheesecomer.rewardstamp.feature.sheet.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.feature.setting.SettingsViewModel
import com.cheesecomer.rewardstamp.model.GoalStampType
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.model.RewardSheet
import com.cheesecomer.rewardstamp.model.RewardStamp
import com.cheesecomer.rewardstamp.model.StampType
import com.cheesecomer.rewardstamp.navigation.BottomTab
import com.cheesecomer.rewardstamp.ui.component.RewardStampBottomBar
import com.cheesecomer.rewardstamp.ui.component.RewardStampScaffold
import com.cheesecomer.rewardstamp.ui.component.dialog.DeleteSheetDialog
import com.cheesecomer.rewardstamp.ui.rewardStampViewModel
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme
import java.time.LocalDateTime

private sealed interface SheetDetailDialog {
    data object Stamp : SheetDetailDialog

    data object Delete : SheetDetailDialog

    data class StampAction(
        val stamp: RewardStamp,
    ) : SheetDetailDialog

    data class ChangeStamp(
        val stamp: RewardStamp,
    ) : SheetDetailDialog
}

@Composable
private fun SheetDetailDialogs(
    activeDialog: SheetDetailDialog?,
    onActiveDialogChange: (SheetDetailDialog?) -> Unit,
    onDeleteRequest: () -> Unit,
    onStampTypeSelect: (StampType) -> Unit,
    onStampTypeChange: (RewardStamp, StampType) -> Unit,
    onStampCancel: (RewardStamp) -> Unit,
) {
    val onDismissRequest = { onActiveDialogChange(null) }
    when (activeDialog) {
        SheetDetailDialog.Delete -> {
            DeleteSheetDialog(
                onDeleteRequest = {
                    onDismissRequest()
                    onDeleteRequest()
                },
                onDismissRequest = onDismissRequest,
            )
        }

        SheetDetailDialog.Stamp -> {
            StampPickerBottomSheet(
                onDismiss = onDismissRequest,
                onSelect = { stampType ->
                    onDismissRequest()
                    onStampTypeSelect(stampType)
                },
            )
        }

        is SheetDetailDialog.StampAction -> {
            StampActionDialog(
                onChangeRequest = {
                    onActiveDialogChange(SheetDetailDialog.ChangeStamp(activeDialog.stamp))
                },
                onCancelStampRequest = {
                    onDismissRequest()
                    onStampCancel(activeDialog.stamp)
                },
                onDismissRequest = onDismissRequest,
            )
        }

        is SheetDetailDialog.ChangeStamp -> {
            StampPickerBottomSheet(
                onDismiss = onDismissRequest,
                onSelect = { stampType ->
                    onDismissRequest()
                    onStampTypeChange(activeDialog.stamp, stampType)
                },
            )
        }

        null -> Unit
    }
}

@Composable
private fun SheetDetailMenu(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    IconButton(
        modifier = Modifier.testTag(SheetDetailTestTags.MENU_BUTTON),
        onClick = {
            showMenu = true
        },
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.common_menu),
        )
    }

    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false },
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.common_edit)) },
            modifier = Modifier.testTag(SheetDetailTestTags.MENU_EDIT_BUTTON),
            onClick = {
                showMenu = false
                onEditClick()
            },
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.common_delete)) },
            modifier = Modifier.testTag(SheetDetailTestTags.MENU_DELETE_BUTTON),
            onClick = {
                showMenu = false
                onDeleteClick()
            },
        )
    }
}

@Composable
private fun SheetNotFound(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    RewardStampScaffold(
        title = "",
        modifier = modifier,
        onBackClick = onBackClick,
    ) { innerPadding ->
        Text(
            text = stringResource(R.string.common_not_found),
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
        )
    }
}

@Composable
private fun SheetDetailBody(
    sheet: RewardSheet,
    stamps: List<RewardStamp>,
    goalStampType: GoalStampType?,
    stampedPosition: Int?,
    stampAnimationState: StampAnimationState,
    onStampClick: () -> Unit,
    onStampLongPress: (RewardStamp) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        ProgressSheet(
            sheet = sheet,
            stamps = stamps,
            goalStampType = goalStampType,
            onStampTypeSelect = onStampClick,
            onStampLongPress = onStampLongPress,
            press = stampAnimationState.press(stampedPosition),
            modifier = Modifier.padding(16.dp),
        )
        GoalCelebration(
            state = stampAnimationState,
            goalStampType = goalStampType,
        )
    }
}

@Composable
internal fun SheetDetailContent(
    sheet: RewardSheet?,
    stamps: List<RewardStamp>,
    exchangeableRewards: List<RewardMilestone>,
    lockedRewards: List<RewardMilestone>,
    modifier: Modifier = Modifier,
    goalStampType: GoalStampType? = null,
    stampedPosition: Int? = null,
    goalCelebrationEnabled: Boolean = true,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onStampTypeSelect: (StampType) -> Unit = {},
    onStampTypeChange: (RewardStamp, StampType) -> Unit = { _, _ -> },
    onStampCancel: (RewardStamp) -> Unit = {},
    onRestartClick: () -> Unit = {},
    onRestartWithEditClick: () -> Unit = {},
) {
    var activeDialog by remember { mutableStateOf<SheetDetailDialog?>(null) }
    if (sheet == null) {
        SheetNotFound(modifier = modifier, onBackClick = onBackClick)
    } else {
        val stampAnimationState =
            rememberStampAnimationState(
                stampedPosition = stampedPosition,
                goalCount = sheet.goalCount,
                goalStampType = goalStampType,
                showcasesGoal = goalCelebrationEnabled,
            )
        SheetDetailDialogs(
            activeDialog = activeDialog,
            onActiveDialogChange = { activeDialog = it },
            onDeleteRequest = onDeleteClick,
            onStampTypeSelect = onStampTypeSelect,
            onStampTypeChange = onStampTypeChange,
            onStampCancel = onStampCancel,
        )

        RewardStampScaffold(
            title = sheet.title,
            modifier = modifier,
            onBackClick = onBackClick,
            actions = {
                SheetDetailMenu(
                    onEditClick = onEditClick,
                    onDeleteClick = {
                        activeDialog = SheetDetailDialog.Delete
                    },
                )
            },
        ) { innerPadding ->
            if (sheet.isCompleted && stampAnimationState.isFinished) {
                CongratulationDialog(
                    onDismissRequest = onRestartClick,
                    onEditButtonClick = onRestartWithEditClick,
                    exchangeableRewards = exchangeableRewards,
                    lockedRewards = lockedRewards,
                    modifier = Modifier.testTag(CongratulationDialogTestTags.ROOT),
                )
            }
            SheetDetailBody(
                sheet = sheet,
                stamps = stamps,
                goalStampType = goalStampType,
                stampedPosition = stampedPosition,
                stampAnimationState = stampAnimationState,
                onStampClick = {
                    activeDialog = SheetDetailDialog.Stamp
                },
                onStampLongPress = { stamp ->
                    activeDialog = SheetDetailDialog.StampAction(stamp)
                },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@ExcludeFromCoverage
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetDetailScreen(
    sheetId: Long,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onRestartWithEditClick: () -> Unit = {},
    viewModel: SheetDetailViewModel =
        rewardStampViewModel {
            SheetDetailViewModel(
                rewardSheetRepository,
                completedRewardSheetRepository,
                rewardStampRepository,
                rewardMilestoneRepository,
                sheetCompletionRepository,
            )
        },
    settingsViewModel: SettingsViewModel =
        rewardStampViewModel {
            SettingsViewModel(settingsRepository)
        },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settingsUiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(sheetId) {
        viewModel.load(sheetId)
    }

    SheetDetailContent(
        sheet = uiState.sheet,
        stamps = uiState.stamps,
        goalStampType = uiState.goalStampType,
        stampedPosition = uiState.stampedPosition,
        goalCelebrationEnabled = settingsUiState.settings.goalCelebrationEnabled,
        exchangeableRewards = uiState.exchangeableRewards,
        lockedRewards = uiState.lockedRewards,
        modifier = modifier,
        onBackClick = onBackClick,
        onEditClick = onEditClick,
        onDeleteClick = {
            viewModel.delete(sheetId) {
                onDeleteClick()
            }
        },
        onStampTypeSelect = @ExcludeFromCoverage { stampType ->
            viewModel.increment(
                sheetId = sheetId,
                stampType = stampType,
            )
        },
        onStampTypeChange = @ExcludeFromCoverage { stamp, stampType ->
            viewModel.changeStampType(stamp, stampType)
        },
        onStampCancel = @ExcludeFromCoverage { stamp ->
            viewModel.cancelStamp(stamp)
        },
        onRestartClick = @ExcludeFromCoverage {
            viewModel.restart(sheetId)
        },
        onRestartWithEditClick = @ExcludeFromCoverage {
            onRestartWithEditClick()
        },
    )
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun SheetDetailContentPreview() {
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
        Scaffold(
            bottomBar = {
                RewardStampBottomBar(
                    selectedTab = BottomTab.Sheets,
                )
            },
        ) { innerPadding ->
            SheetDetailContent(
                sheet =
                    RewardSheet(
                        id = 0,
                        title = "おてつだい",
                        goalCount = 10,
                        currentCount = 9,
                    ),
                stamps =
                    listOf(
                        rewardStamp(0),
                        rewardStamp(1),
                        rewardStamp(2),
                        rewardStamp(3),
                        rewardStamp(4),
                        rewardStamp(5),
                        rewardStamp(6),
                        rewardStamp(7),
                        rewardStamp(8),
                    ),
                goalStampType = GoalStampType.Bear1,
                exchangeableRewards = emptyList(),
                lockedRewards = emptyList(),
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
