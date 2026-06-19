package com.cheesecomer.rewardseal.feature.sheet.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cheesecomer.rewardseal.RewardSealApplication
import com.cheesecomer.rewardseal.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardseal.ui.RewardSealTextFieldDefaults
import com.cheesecomer.rewardseal.ui.theme.RewardSealTheme

@ExcludeFromCoverage
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SheetEditScreenHeader(
    sheetId: Long?,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            if (sheetId == null) {
                Text("シートを作る")
            } else {
                Text("シートを編集")
            }
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
private fun SheetNameSection(
    title: String,
    onTitleChange: (String) -> Unit,
    canEdit: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        SheetEditSectionTitle(
            text = "なにを がんばる？",
            modifier = Modifier.padding(start = 16.dp),
        )
        Spacer(modifier = Modifier.size(8.dp))
        if (canEdit) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = { Text("はみがき") },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .testTag("SheetEditScreen.TitleTextField"),
                singleLine = true,
                colors = RewardSealTextFieldDefaults.colors(),
            )
        } else {
            Text(
                text = title,
                modifier =
                    Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                        .testTag("SheetEditScreen.TitleLabel"),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
internal fun SheetEditContent(
    sheetId: Long?,
    title: String,
    goalCount: Int,
    milestones: List<RewardMilestoneUiState>,
    modifier: Modifier = Modifier,
    canSave: Boolean = false,
    onSaveClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onIncrementGoalCount: () -> Unit = {},
    onDecrementGoalCount: () -> Unit = {},
    onCreateMilestoneClick: (RewardMilestoneUiState) -> Unit = {},
    onUpdateMilestoneClick: (index: Int, RewardMilestoneUiState) -> Unit = { _, _ -> },
    onRemoveMilestoneClick: (index: Int) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SheetEditScreenHeader(sheetId, onBackClick = onBackClick)
        },
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .testTag("SheetEditScreen.List"),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                SheetNameSection(
                    title = title,
                    onTitleChange = onTitleChange,
                    canEdit = sheetId == null,
                )
            }

            item {
                GoalCountPicker(
                    goalCount,
                    onPlusClick = onIncrementGoalCount,
                    onMinusClick = onDecrementGoalCount,
                )
            }

            item {
                RewardMilestonesSection(
                    milestones = milestones,
                    onRemoveClick = onRemoveMilestoneClick,
                    onCreateClick = onCreateMilestoneClick,
                    onUpdateClick = onUpdateMilestoneClick,
                )
            }

            item {
                Button(
                    onClick = onSaveClick,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .testTag("SheetEditScreen.Save"),
                    enabled = canSave,
                ) {
                    Text("保存")
                }
            }
        }
    }
}

@ExcludeFromCoverage
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

    SheetEditContent(
        sheetId = sheetId,
        title = viewModel.uiState.title,
        goalCount = viewModel.uiState.goalCount,
        milestones = viewModel.uiState.milestones,
        modifier = modifier,
        canSave = viewModel.canSave(),
        onSaveClick = {
            viewModel.save {
                onSaveClick()
            }
        },
        onBackClick = onBackClick,
        onTitleChange = { viewModel.updateTitle(it) },
        onIncrementGoalCount = {
            viewModel.incrementGoalCount()
        },
        onDecrementGoalCount = {
            viewModel.decrementGoalCount()
        },
        onUpdateMilestoneClick = viewModel::updateMilestone,
        onCreateMilestoneClick = viewModel::createMilestone,
        onRemoveMilestoneClick = viewModel::removeMilestone,
    )
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun SheetEditScreenPreview() {
    RewardSealTheme {
        SheetEditContent(
            sheetId = 1L,
            title = "おかたずけ",
            goalCount = 1,
            milestones =
                listOf(
                    RewardMilestoneUiState(
                        requiredCompletions = "1",
                        reward = "スーパーカップ",
                    ),
                    RewardMilestoneUiState(
                        requiredCompletions = "2",
                        reward = "ハーゲンダッツ",
                    ),
                ),
            modifier = Modifier.fillMaxHeight(),
        )
    }
}
