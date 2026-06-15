package com.cheesecomer.rewardseal.ui.screen.sheetdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication
import com.cheesecomer.rewardseal.model.RewardSheet
import com.cheesecomer.rewardseal.model.StampType
import com.cheesecomer.rewardseal.ui.component.RewardBoardState
import com.cheesecomer.rewardseal.ui.component.RewardBoardView
import com.cheesecomer.rewardseal.ui.component.StampTypeGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetDetailScreen(
    sheetId: Long,
    onBackClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onRestartWithEditClick: (sheetId: Long) -> Unit,
) {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    val viewModel: SheetDetailViewModel = viewModel(
        factory = SheetDetailViewModel.factory(
            application.rewardSheetRepository,
            application.completedRewardSheetRepository,
            application.rewardStampRepository
        )
    )
    var showStampDialog by remember { mutableStateOf(false) }

    LaunchedEffect(sheetId) {
        viewModel.load(sheetId)
    }
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    val uiState = viewModel.uiState
    val sheet = uiState.sheet
    if (sheet == null) {
        Text("見つかりません")
        return
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("シートを削除しますか？")
            },
            text = {
                Text("このシートは削除されます。")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.delete(sheetId)
                        onDeleteClick()
                    }
                ) {
                    Text("削除")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("キャンセル")
                }
            }
        )
    }
    if (showStampDialog) {
        AlertDialog(
            onDismissRequest = {
                showStampDialog = false
            },
            title = {
                Text("スタンプを選ぶ")
            },
            text = {
                StampTypeGrid(
                    selectedStampType = StampType.Star,
                    onStampTypeClick = { stampType ->
                        showStampDialog = false
                        viewModel.increment(
                            sheetId = sheetId,
                            stampType = stampType,
                        )
                    }
                )
            },
            confirmButton = {},
            dismissButton = {
                TextButton(
                    onClick = {
                        showStampDialog = false
                    }
                ) {
                    Text("キャンセル")
                }
            }
        )
    }

    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(sheet.title)
            },
            navigationIcon = {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "戻る"
                    )
                }
            }
        )
        if (sheet.isCompleted) {
            CompletedSheetActions(
                sheet = sheet,
                onRestartClick = {
                    viewModel.restart(sheetId)
                },
                onRestartWithEditClick = {
                    onRestartWithEditClick(sheetId)
                },
                onReceiveLaterClick = {
                    onBackClick()
                },
            )
        } else {
            Text("${sheet.currentCount} / ${sheet.goalCount}")
            Button(
                onClick = {
                    showStampDialog = true
                },
                enabled = sheet.currentCount < sheet.goalCount,
            ) {
                Text("スタンプを押す")
            }
            Button(
                onClick = {
                    showDeleteDialog = true
                }
            ) {
                Text("削除")
            }
            if (sheet.currentCount >= sheet.goalCount) {
                Text("ごほうび達成！ ${sheet.reward}")
            }
            RewardBoardView(
                board = RewardBoardState(
                    title = sheet.title,
                    currentCount = sheet.currentCount,
                    goalCount = sheet.goalCount,
                ),
                stamps = uiState.stamps,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun CompletedSheetActions(
    sheet: RewardSheet,
    onRestartClick: () -> Unit,
    onRestartWithEditClick: () -> Unit,
    onReceiveLaterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "🎉 ごほうび達成！",
            style = MaterialTheme.typography.headlineMedium,
        )

        Text("${sheet.title} を ${sheet.goalCount}回 がんばりました")

        Text(
            text = "ごほうび：${sheet.reward}",
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            text = "ごほうびを受け取ったら、\n未受領ごほうび一覧からチェックできます。",
            style = MaterialTheme.typography.bodyMedium,
        )

        Text(
            text = "次のチャレンジをはじめますか？",
            style = MaterialTheme.typography.titleMedium,
        )

        Button(
            onClick = onRestartClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("同じ内容でもう一度がんばる")
        }

        Button(
            onClick = onRestartWithEditClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("ごほうびや回数を変えてがんばる")
        }

        TextButton(
            onClick = onReceiveLaterClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("あとでごほうびをうけとる")
        }
    }
}