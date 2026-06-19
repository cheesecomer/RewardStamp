package com.cheesecomer.rewardseal.feature.sheet.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.model.RewardSheet
import com.cheesecomer.rewardseal.model.RewardStamp
import com.cheesecomer.rewardseal.model.StampType
import com.cheesecomer.rewardseal.navigation.BottomTab
import com.cheesecomer.rewardseal.ui.component.RewardSealBottomBar
import com.cheesecomer.rewardseal.ui.component.dialog.DeleteSheetDialog
import com.cheesecomer.rewardseal.ui.component.dialog.SelectStampDialog
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme
import java.time.LocalDateTime

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
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    var showMenu by remember { mutableStateOf(false) }
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
        actions = {
            IconButton(
                modifier = Modifier.testTag("SheetDetailScreen.MenuButton"),
                onClick = {
                    showMenu = true
                },
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "メニュー",
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
            ) {
                DropdownMenuItem(
                    text = { Text("編集") },
                    modifier = Modifier.testTag("SheetDetailScreen.Menu.EditButton"),
                    onClick = {
                        showMenu = false
                        onEditClick()
                    },
                )
                DropdownMenuItem(
                    text = { Text("削除") },
                    modifier = Modifier.testTag("SheetDetailScreen.Menu.DeleteButton"),
                    onClick = {
                        showMenu = false
                        onDeleteClick()
                    },
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
    var activeDialog by remember { mutableStateOf<SheetDetailDialog?>(null) }
    if (sheet == null) {
        SheetNotFound(modifier = modifier)
    } else {
        SheetDetailDialogs(
            activeDialog = activeDialog,
            onDismissRequest = {
                activeDialog = null
            },
            onDeleteRequest = {
                activeDialog = null
                onDeleteClick()
            },
            onStampTypeSelect = { stampType ->
                activeDialog = null
                onStampTypeSelect(stampType)
            },
        )
        Scaffold(
            modifier = modifier,
            topBar = {
                SheetDetailHeader(
                    sheet,
                    onBackClick = onBackClick,
                    onEditClick = onEditClick,
                    onDeleteClick = {
                        activeDialog = SheetDetailDialog.Delete
                    },
                )
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
                        onStampTypeSelect = {
                            activeDialog = SheetDetailDialog.Stamp
                        },
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

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun SheetDetailContentPreview() {
    val rewardStamp: (Int) -> RewardStamp = { position ->
        RewardStamp(
            id = position.toLong(),
            sheetId = 1,
            completedRewardSheetId = null,
            position = position,
            stampedAt = LocalDateTime.now().minusSeconds(10 - position.toLong()),
            stampType = StampType.Star,
        )
    }
    RewardSealTheme {
        Scaffold(
            bottomBar = {
                RewardSealBottomBar(
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
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
