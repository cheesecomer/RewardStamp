package com.cheesecomer.rewardseal.feature.sheet.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.model.RewardSheet
import com.cheesecomer.rewardseal.model.RewardStamp
import com.cheesecomer.rewardseal.model.StampType
import com.cheesecomer.rewardseal.ui.component.RewardBoardState
import com.cheesecomer.rewardseal.ui.component.RewardBoardView
import com.cheesecomer.rewardseal.ui.component.dialog.DeleteSheetDialog
import com.cheesecomer.rewardseal.ui.component.dialog.SelectStampDialog

@ExcludeFromCoverage
@Composable
private fun CompletedSheetActions(
    sheet: RewardSheet,
    onRestartClick: () -> Unit,
    onRestartWithEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "\uD83C\uDF89 シートが満タンになりました！",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text("${sheet.title} を ${sheet.goalCount}回 がんばってシートを満タンにしました。")

        Text(
            text = "ごほうび交換画面で、ためたシートとごほうびを交換できます。",
            style = MaterialTheme.typography.bodyMedium,
        )

        Button(
            onClick = onRestartClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("もっとがんばる")
        }

        Button(
            onClick = onRestartWithEditClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("ごほうびや回数を変えてがんばる")
        }
    }
}

private enum class SheetDetailDialog {
    Stamp,
    Delete,
}

@Composable
private fun SheetDetailDialogs(
    activeDialog: SheetDetailDialog?,
    onDismissRequest: () -> Unit,
    onDeleteRequest: () -> Unit,
    onStampTypeSelect: (StampType) -> Unit,
) {
    when (activeDialog) {
        SheetDetailDialog.Delete -> {
            DeleteSheetDialog(
                onDeleteRequest = onDeleteRequest,
                onDismissRequest = onDismissRequest,
            )
        }

        SheetDetailDialog.Stamp -> {
            SelectStampDialog(
                onDismissRequest = onDismissRequest,
                onStampTypeSelect = onStampTypeSelect,
            )
        }

        null -> Unit
    }
}

@Composable
private fun ProgressSheet(
    sheet: RewardSheet,
    stamps: List<RewardStamp>,
    onEditRequest: () -> Unit,
    onDeleteRequest: () -> Unit,
    onStampTypeSelect: (StampType) -> Unit,
) {
    var activeDialog by remember { mutableStateOf<SheetDetailDialog?>(null) }

    SheetDetailDialogs(
        activeDialog = activeDialog,
        onDismissRequest = {
            activeDialog = null
        },
        onDeleteRequest = {
            activeDialog = null
            onDeleteRequest()
        },
        onStampTypeSelect = { stampType ->
            activeDialog = null
            onStampTypeSelect(stampType)
        },
    )

    Column {
        Text("${sheet.currentCount} / ${sheet.goalCount}")
        Button(
            onClick = {
                activeDialog = SheetDetailDialog.Stamp
            },
            enabled = sheet.currentCount < sheet.goalCount,
        ) {
            Text("スタンプを押す")
        }

        Button(
            onClick = onEditRequest,
        ) {
            Text("編集")
        }

        Button(
            onClick = {
                activeDialog = SheetDetailDialog.Delete
            },
        ) {
            Text("削除")
        }

        RewardBoardView(
            board =
                RewardBoardState(
                    title = sheet.title,
                    currentCount = sheet.currentCount,
                    goalCount = sheet.goalCount,
                ),
            stamps = stamps,
            modifier = Modifier.weight(1f),
        )
    }
}

@ExcludeFromCoverage
@Composable
private fun sheetDetailViewModel(): SheetDetailViewModel {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    return viewModel<SheetDetailViewModel>(
        factory =
            SheetDetailViewModel.factory(
                application.rewardSheetRepository,
                application.completedRewardSheetRepository,
                application.rewardStampRepository,
            ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SheetDetailHeader(
    sheet: RewardSheet,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(sheet.title)
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "戻る",
                )
            }
        },
    )
}

@Composable
private fun SheetNotFound(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Text(
            text = "見つかりません",
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
        )
    }
}

@Composable
internal fun SheetDetailContent(
    sheet: RewardSheet?,
    stamps: List<RewardStamp>,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onStampTypeSelect: (StampType) -> Unit = {},
    onRestartClick: () -> Unit = {},
    onRestartWithEditClick: () -> Unit = {},
) {
    if (sheet == null) {
        SheetNotFound(modifier = modifier)
    } else {
        Scaffold(
            modifier = modifier,
            topBar = {
                SheetDetailHeader(sheet, onBackClick = onBackClick)
            },
        ) { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .padding(16.dp),
            ) {
                if (sheet.isCompleted) {
                    CompletedSheetActions(
                        sheet = sheet,
                        onRestartClick = onRestartClick,
                        onRestartWithEditClick = onRestartWithEditClick,
                    )
                } else {
                    ProgressSheet(
                        sheet = sheet,
                        stamps = stamps,
                        onEditRequest = onEditClick,
                        onDeleteRequest = onDeleteClick,
                        onStampTypeSelect = onStampTypeSelect,
                    )
                }
            }
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
    viewModel: SheetDetailViewModel = sheetDetailViewModel(),
) {
    LaunchedEffect(sheetId) {
        viewModel.load(sheetId)
    }

    SheetDetailContent(
        sheet = viewModel.uiState.sheet,
        stamps = viewModel.uiState.stamps,
        modifier = modifier,
        onBackClick = onBackClick,
        onEditClick = onEditClick,
        onDeleteClick = {
            viewModel.delete(sheetId) {
                onDeleteClick()
            }
        },
        onStampTypeSelect = { stampType ->
            viewModel.increment(
                sheetId = sheetId,
                stampType = stampType,
            )
        },
        onRestartClick = {
            viewModel.restart(sheetId)
        },
        onRestartWithEditClick = {
            onRestartWithEditClick()
        },
    )
}
