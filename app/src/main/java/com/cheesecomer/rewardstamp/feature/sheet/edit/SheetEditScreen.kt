package com.cheesecomer.rewardstamp.feature.sheet.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cheesecomer.rewardstamp.R
import com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage
import com.cheesecomer.rewardstamp.model.RewardMilestone
import com.cheesecomer.rewardstamp.ui.RewardStampTextFieldDefaults
import com.cheesecomer.rewardstamp.ui.component.RewardStampScaffold
import com.cheesecomer.rewardstamp.ui.component.SectionTitle
import com.cheesecomer.rewardstamp.ui.rewardStampViewModel
import com.cheesecomer.rewardstamp.ui.theme.RewardStampTheme

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
        SectionTitle(
            text = stringResource(R.string.sheet_edit_name_label),
            modifier = Modifier.padding(start = 16.dp),
        )
        Spacer(modifier = Modifier.size(8.dp))
        if (canEdit) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = { Text(stringResource(R.string.sheet_edit_name_placeholder)) },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .testTag(SheetEditTestTags.TITLE_TEXT_FIELD),
                singleLine = true,
                colors = RewardStampTextFieldDefaults.colors(),
            )
        } else {
            Text(
                text = title,
                modifier =
                    Modifier
                        .padding(start = 16.dp, bottom = 16.dp)
                        .testTag(SheetEditTestTags.TITLE_LABEL),
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
    milestones: List<RewardMilestone>,
    modifier: Modifier = Modifier,
    canSave: Boolean = false,
    onSaveClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onIncrementGoalCount: () -> Unit = {},
    onDecrementGoalCount: () -> Unit = {},
    onCreateMilestoneClick: (RewardMilestone) -> Unit = {},
    onUpdateMilestoneClick: (index: Int, RewardMilestone) -> Unit = { _, _ -> },
    onRemoveMilestoneClick: (index: Int) -> Unit = {},
) {
    RewardStampScaffold(
        title =
            stringResource(
                if (sheetId == null) R.string.sheet_edit_title_create else R.string.sheet_edit_title_edit,
            ),
        modifier = modifier,
        onBackClick = onBackClick,
    ) { innerPadding ->
        LazyColumn(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .testTag(SheetEditTestTags.LIST),
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
                            .testTag(SheetEditTestTags.SAVE),
                    enabled = canSave,
                ) {
                    Text(stringResource(R.string.common_save))
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
    viewModel: SheetEditViewModel =
        rewardStampViewModel {
            SheetEditViewModel(
                rewardSheetRepository,
                rewardMilestoneRepository,
            )
        },
    onSaveClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(sheetId) {
        if (sheetId != null) {
            viewModel.load(sheetId)
        }
    }

    SheetEditContent(
        sheetId = sheetId,
        title = uiState.title,
        goalCount = uiState.goalCount,
        milestones = uiState.milestones,
        modifier = modifier,
        canSave = uiState.canSave(),
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
private fun NewSheetScreenPreview() {
    RewardStampTheme {
        SheetEditContent(
            sheetId = null,
            title = "おかたずけ",
            goalCount = 1,
            milestones =
                listOf(
                    RewardMilestone(
                        id = 0L,
                        sheetId = 1L,
                        requiredSheetCount = 1,
                        reward = "スーパーカップ",
                    ),
                    RewardMilestone(
                        id = 0L,
                        sheetId = 1L,
                        requiredSheetCount = 2,
                        reward = "ハーゲンダッツ",
                    ),
                ),
            modifier = Modifier.fillMaxHeight(),
        )
    }
}

@ExcludeFromCoverage
@Preview(showBackground = true)
@Suppress("MagicNumber")
@Composable
private fun SheetEditScreenPreview() {
    RewardStampTheme {
        SheetEditContent(
            sheetId = 1L,
            title = "おかたずけ",
            goalCount = 1,
            milestones =
                listOf(
                    RewardMilestone(
                        id = 0L,
                        sheetId = 1L,
                        requiredSheetCount = 1,
                        reward = "スーパーカップ",
                    ),
                    RewardMilestone(
                        id = 0L,
                        sheetId = 1L,
                        requiredSheetCount = 2,
                        reward = "ハーゲンダッツ",
                    ),
                ),
            modifier = Modifier.fillMaxHeight(),
        )
    }
}
