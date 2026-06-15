package com.cheesecomer.rewardseal.ui.screen.unreceivedrewardlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnreceivedRewardListScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onRestartWithEditClick: (sheetId: Long) -> Unit,
) {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    val viewModel: UnreceivedRewardListViewModel = viewModel(
        factory = UnreceivedRewardListViewModel.factory(
            application.rewardSheetRepository,
            application.completedRewardSheetRepository,
            application.rewardMilestoneRepository
        )
    )
    val uiState = viewModel.uiState
    val sheets = uiState.sheets
    var showExchangeDialog by remember { mutableLongStateOf(0L) }
    if (showExchangeDialog != 0L) {
        val sheet = sheets.find { it.id == showExchangeDialog }!!
        if (sheet.exchangeableMilestones.size == 1) {
            val milestone = sheet.exchangeableMilestones[0]
            AlertDialog(
                onDismissRequest = {
                    showExchangeDialog = 0L
                },
                title = {
                    Text("交換しますか？")
                },
                text = {
                    Text("${milestone.reward} と交換しますか？")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExchangeDialog = 0L
                            viewModel.receiveReward(sheet.id, milestone)
                        }
                    ) {
                        Text("交換する")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showExchangeDialog = 0L
                        }
                    ) {
                        Text("キャンセル")
                    }
                }
            )
        } else {
            val milestones = sheet.exchangeableMilestones
            AlertDialog(
                onDismissRequest = {
                    showExchangeDialog = 0L
                },
                title = {
                    Text("ごほうびを選んでね")
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        milestones.forEach { milestone ->
                            TextButton(
                                onClick = {
                                    showExchangeDialog = 0L
                                    viewModel.receiveReward(sheet.id, milestone)
                                },
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Text(
                                    text = "${milestone.reward}（${milestone.requiredCompletions}枚）",
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(
                        onClick = {
                            showExchangeDialog = 0L
                        },
                    ) {
                        Text("キャンセル")
                    }
                },
            )
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text("交換できるごほうび")
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

        if (sheets.isEmpty()) {
            Text("交換できるごほうびはありません")
            return
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(sheets) { sheet ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = sheet.title,
                            style = MaterialTheme.typography.titleLarge,
                        )

                        Text("未交換シート ${sheet.unconsumedCompletedCount}枚")
                        Text(
                            text = "交換可能",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        sheet.exchangeableMilestones.forEach { milestone ->
                            Text("${milestone.requiredCompletions}枚 ${milestone.reward}")
                        }

                        if (sheet.nextMilestone != null) {
                            Text("あと ${sheet.nextMilestone.requiredCompletions - sheet.unconsumedCompletedCount}枚 で ${sheet.nextMilestone.reward} と交換できるよ")
                        }

                        Button(
                            onClick = {
                                showExchangeDialog = sheet.id
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("受け取った")
                        }
                    }
                }
            }
        }
    }
}