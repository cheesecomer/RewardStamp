package com.cheesecomer.rewardseal.feature.sheet.edit

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
import androidx.compose.material3.Scaffold
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

@Composable
private fun sheetEditViewModel(): SheetEditViewModel {
    val application =
        LocalContext.current.applicationContext as RewardSealApplication
    return viewModel(
        factory =
            SheetEditViewModel.factory(
                application.rewardSheetRepository,
                application.rewardMilestoneRepository,
            ),
    )
}

@Composable
fun GoalCountPicker(
    goalCount: Int,
    modifier: Modifier = Modifier,
    onPlusClick: () -> Unit = {},
    onMinusClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                        if (goalCount > 1) {
                            onMinusClick
                        }
                    },
                ) {
                    Text("−")
                }

                Text(
                    text = "$goalCount 回",
                    style = MaterialTheme.typography.headlineSmall,
                )

                TextButton(
                    onClick = onPlusClick,
                ) {
                    Text("＋")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetEditScreenHeader(
    sheetId: Long?,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            if (sheetId == null) {
                Text("シートを作る")
            } else {
                Text("ごほうびや回数を変える")
            }
        },
        modifier = modifier,
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
fun TitleForm(
    value: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit = {},
) {
    if (enabled) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("なにを がんばる？") },
            placeholder = { Text("はみがき") },
            modifier = modifier.fillMaxWidth(),
            singleLine = true,
        )
    } else {
        Text(
            modifier = modifier,
            text = "${value}をがんばる",
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
fun SheetEditScreen(
    modifier: Modifier = Modifier,
    sheetId: Long? = null,
    viewModel: SheetEditViewModel = sheetEditViewModel(),
    onSaveClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    LaunchedEffect(sheetId) {
        if (sheetId != null) {
            viewModel.load(sheetId)
        }
    }

    val uiState = viewModel.uiState

    Scaffold(
        modifier = modifier,
        topBar = {
            SheetEditScreenHeader(sheetId, onBackClick = onBackClick)
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            TitleForm(
                value = uiState.title,
                onValueChange = { viewModel.updateTitle(it) },
                enabled = sheetId == null,
            )

            GoalCountPicker(
                uiState.goalCount,
                onPlusClick = {
                    viewModel.incrementGoalCount()
                },
                onMinusClick = {
                    viewModel.decrementGoalCount()
                },
            )

            MilestoneFormList(
                milestones = uiState.milestones,
                onRequiredCompletionsChange = viewModel::updateMilestoneRequiredCompletions,
                onRewardChange = viewModel::updateMilestoneReward,
                onAddClick = viewModel::addMilestone,
                onRemoveClick = viewModel::removeMilestone,
            )

            Button(
                onClick = {
                    viewModel.save {
                        onSaveClick()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.title.isNotBlank() && uiState.hasReward(),
            ) {
                Text("保存")
            }
        }
    }
}
