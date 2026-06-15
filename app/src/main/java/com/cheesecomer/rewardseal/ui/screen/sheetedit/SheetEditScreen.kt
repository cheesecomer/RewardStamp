package com.cheesecomer.rewardseal.ui.screen.sheetedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetEditScreen(
    modifier: Modifier = Modifier,
    sheetId: Long? = null,
    onSaveClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    val viewModel: SheetEditViewModel = viewModel(
        factory = SheetEditViewModel.factory(
            application.rewardSheetRepository,
            application.rewardMilestoneRepository
        )
    )

    LaunchedEffect(sheetId) {
        if (sheetId != null) {
            viewModel.load(sheetId)
        }
    }

    val uiState = viewModel.uiState

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CenterAlignedTopAppBar(
            title = {
                if (sheetId == null) {
                    Text("シートを作る")
                } else {
                    Text("ごほうびや回数を変える")
                }
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

        if (sheetId == null) {
            OutlinedTextField(
                value = uiState.title,
                onValueChange = { viewModel.updateTitle(it) },
                label = { Text("なにを がんばる？") },
                placeholder = { Text("はみがき") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
        } else {
            Text(
                text = "${uiState.title}をがんばる",
                style = MaterialTheme.typography.titleLarge
            )
        }

//        OutlinedTextField(
//            value = uiState.reward,
//            onValueChange = { viewModel.updateReward(it) },
//            label = { Text("ごほうび") },
//            placeholder = { Text("アイス") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true,
//        )

        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "何回がんばる？",
                    style = MaterialTheme.typography.titleMedium,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        onClick = {
                            if (uiState.goalCount > 1) {
                                viewModel.decrementGoalCount()
                            }
                        },
                    ) {
                        Text("−")
                    }

                    Text(
                        text = "${uiState.goalCount} 回",
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    TextButton(
                        onClick = {
                            viewModel.incrementGoalCount()
                        },
                    ) {
                        Text("＋")
                    }
                }
            }
        }

        MilestoneFormList(
            milestones = uiState.milestones,
            onRequiredCompletionsChange = viewModel::updateMilestoneRequiredCompletions,
            onRewardChange = viewModel::updateMilestoneReward,
            onAddClick = viewModel::addMilestone,
            onRemoveClick = viewModel::removeMilestone,
        )

        Button(
            onClick = {
                viewModel.save(onSaveClick)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.title.isNotBlank() && uiState.hasReward(),
        ) {
            Text("保存")
        }
    }
}